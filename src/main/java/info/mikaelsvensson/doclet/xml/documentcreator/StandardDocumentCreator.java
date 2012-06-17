package info.mikaelsvensson.doclet.xml.documentcreator;

import com.sun.javadoc.*;
import info.mikaelsvensson.doclet.shared.*;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Pattern;

public class StandardDocumentCreator extends AbstractDocumentCreator {

    /**
     * @documentcreatorparameter
     */
    public static final String PARAMETER_SHOW_ANNOTATIONS = "showAnnotations";
    /**
     * @documentcreatorparameter
     */
    public static final String PARAMETER_SHOW_TYPE_PARAMETERS = "showTypeParameters";
    /**
     * @documentcreatorparameter
     */
    public static final String PARAMETER_SHOW_INHERITED_INTERFACES = "showInheritedInterfaces";
    /**
     * @documentcreatorparameter
     */
    public static final String PARAMETER_SHOW_FIELDS = "showFields";
    /**
     * @documentcreatorparameter
     */
    public static final String PARAMETER_TEXT_ONLY_COMMENTS = "textOnlyComments";
    /**
     * @documentcreatorparameter
     */
    public static final String PARAMETER_SHOW_ALL_TAGS = "showAllTags";

    private static final String ATTR_NAME = "name";
    private static final String ATTR_Q_NAME = "qualified-name";

    private TagHandler[] tagHandlers = new TagHandler[]{new CodeTagHandler(), new ImageTagHandler(), new LinkTagHandler()};
    public static final Pattern COMMENT_PARAGRAPH = Pattern.compile("<(p|br)\\s*/\\s*>");
    private boolean showAnnotations;
    private boolean showTypeParameters;
    private boolean showInheritedInterfaces;
    private boolean showFields;
    private boolean textOnlyComments;
    private boolean showAllTags;

    public StandardDocumentCreator(final Map<String, String> parameters) throws ParserConfigurationException {
        super();
        showAnnotations = isBooleanSet(PARAMETER_SHOW_ANNOTATIONS, parameters);
        showTypeParameters = isBooleanSet(PARAMETER_SHOW_TYPE_PARAMETERS, parameters);
        showInheritedInterfaces = isBooleanSet(PARAMETER_SHOW_INHERITED_INTERFACES, parameters);
        showFields = isBooleanSet(PARAMETER_SHOW_FIELDS, parameters);
        textOnlyComments = isBooleanSet(PARAMETER_TEXT_ONLY_COMMENTS, parameters);
        showAllTags = isBooleanSet(PARAMETER_SHOW_ALL_TAGS, parameters);
    }

    @Override
    public Document generateDocument(final RootDoc root) {
        DocumentWrapper dw = new DocumentWrapper(createDocument("packages"));

        Map<String, ElementWrapper> packageElements = new HashMap<String, ElementWrapper>();

        for (ClassDoc cls : root.classes()) {
            String className = cls.name();
            String packageName = cls.containingPackage().name();

            ElementWrapper packageEl = getPackageElement(packageElements, packageName, dw);

            ElementWrapper clsEl = packageEl.addChild(cls.isEnum() ? "enum" : "class",
                    ATTR_NAME, className,
                    ATTR_Q_NAME, cls.qualifiedName(),
                    "access", getAccess(cls),
                    "serializable", Boolean.toString(cls.isSerializable()));
            if (null != cls.superclass()) {
                clsEl.setAttribute("extends", cls.superclass().qualifiedName());
            }

            if (cls.isEnum()) {
                for (FieldDoc enumConstant : cls.enumConstants()) {
                    ElementWrapper enumConstantEl = clsEl.addChild("value", ATTR_NAME, enumConstant.name());

                    addComment(enumConstantEl, enumConstant.inlineTags(), root);
                }
            } else {
                clsEl.setAttribute("abstract", Boolean.toString(cls.isAbstract()));
                clsEl.setAttribute("interface", Boolean.toString(cls.isInterface()));

                addMethods(cls, clsEl, root);
            }

            addComment(clsEl, cls.inlineTags(), root);

            if (showAnnotations) {
                addAnnotations(clsEl, cls);
            }

            if (showFields) {
                addFields(cls, clsEl, root);
            }

            addInterfaces(clsEl, cls);

            addImplementations(clsEl, cls, root);

            addReferences(clsEl, cls);

            if (showAllTags) {
                addTags(cls, clsEl);
            }
        }
        return dw.getDocument();
    }

    private void addTags(final Doc javadocItem, final ElementWrapper el) {
        ElementWrapper tagsEl = el.addChild("tags");
        for (Tag tag : javadocItem.tags()) {
            tagsEl.addChild("tag", ATTR_NAME, tag.name().substring(1), "text", tag.text());
        }
    }

    private boolean isBooleanSet(final String key, final Map<String, String> parameters) {
        return parameters.containsKey(key) && Boolean.valueOf(parameters.get(key));
    }

    private ElementWrapper getPackageElement(Map<String, ElementWrapper> packageElements, String packageName, ElementWrapper packagesElement) {
        if (!packageElements.containsKey(packageName)) {
            ElementWrapper el = packagesElement.addChild("package").setAttribute(ATTR_NAME, packageName);
            packageElements.put(packageName, el);
            return el;
        } else {
            return packageElements.get(packageName);
        }

    }

    private void addImplementations(ElementWrapper clsEl, ClassDoc cls, RootDoc root) {
        if (cls.isInterface()) {
            ElementWrapper implementationsEl = clsEl.addChild("implementations");
            for (ClassDoc impl : getImplementations(cls, root)) {
                implementationsEl.addChild("implementation", ATTR_Q_NAME, impl.qualifiedName());
            }
        }
    }

    private void addReferences(ElementWrapper javadocItemEl, Doc javadocItem) {
        if (javadocItem.seeTags().length > 0) {
            ElementWrapper referencesEl = javadocItemEl.addChild("references");
            for (SeeTag seeTag : javadocItem.seeTags()) {
                String refClass = seeTag.referencedClass() != null ? seeTag.referencedClass().qualifiedName() : null;
                String refMember = seeTag.referencedMember() != null ? seeTag.referencedMember().name() : null;
                if (refClass != null) {
                    ElementWrapper refEl = referencesEl.addChild("reference", "class", refClass);
                    if (refMember != null) {
                        refEl.setAttribute("member", refMember);
                    }
                }
            }
        }
    }

    private Collection<ClassDoc> getImplementations(ClassDoc interfaceClass, RootDoc root) {
        Collection<ClassDoc> implementations = new LinkedList<ClassDoc>();
        for (ClassDoc potentialImplementation : root.classes()) {
            if (isImplementation(interfaceClass, potentialImplementation)) {
                implementations.add(potentialImplementation);
            }
        }
        return implementations;
    }

    private boolean isImplementation(ClassDoc interfaceClass, ClassDoc potentialImplementationClass) {
        boolean implementsInterface = false;
        for (ClassDoc implementedInterface : potentialImplementationClass.interfaces()) {
            if (implementedInterface.qualifiedName().equals(interfaceClass.qualifiedName())) {
                implementsInterface = true;
            }
        }
        return implementsInterface;
    }

    private void addMethods(ClassDoc cls, ElementWrapper clsEl, RootDoc root) {
        ElementWrapper methodsEl = clsEl.addChild("methods");
        for (MethodDoc m : cls.methods()) {
            String methodName = m.name();
            Type returnType = m.returnType();
            ElementWrapper methodEl = methodsEl.addChild("method",
                    ATTR_NAME, methodName,
                    "abstract", Boolean.toString(m.isAbstract()),
                    "constructor", Boolean.toString(m.isConstructor()),
                    "final", Boolean.toString(m.isFinal()),
                    "static", Boolean.toString(m.isStatic()),
                    "access", getAccess(m),
                    "partofproperty", Boolean.toString(isPartOfProperty(m, cls)),
                    "synchronized", Boolean.toString(m.isSynchronized()));

            addParameters(m, methodEl);

            addTypeInformation(methodEl, returnType, "returns");

            addComment(methodEl, m.inlineTags(), root);

            if (showAnnotations) {
                addAnnotations(methodEl, m);
            }

            if (showAllTags) {
                addTags(m, methodEl);
            }
            addThrownExceptions(methodEl, m, root);

            addReferences(methodEl, m);
        }
    }

    private void addFields(ClassDoc cls, ElementWrapper clsEl, RootDoc root) {
        ElementWrapper fieldsEl = clsEl.addChild("fields");
        for (FieldDoc f : cls.fields()) {
            String fieldName = f.name();
            ElementWrapper fieldEl = fieldsEl.addChild("field",
                    ATTR_NAME, fieldName,
                    "transient", Boolean.toString(f.isTransient()),
                    "volatile", Boolean.toString(f.isVolatile()),
                    "final", Boolean.toString(f.isFinal()),
                    "static", Boolean.toString(f.isStatic()),
                    "access", getAccess(f));

            if (null != f.constantValue()) {
                fieldEl.setAttribute("constant-value", f.constantValue().toString());
            }

            addTypeInformation(fieldEl, f.type(), "type");

            addComment(fieldEl, f.inlineTags(), root);

            if (showAnnotations) {
                addAnnotations(fieldEl, f);
            }
            if (showAllTags) {
                addTags(f, fieldEl);
            }

            addReferences(fieldEl, f);
        }
    }

    private boolean isPartOfProperty(MethodDoc needleMethod, ClassDoc cls) {
        String needleMethodName = needleMethod.name();
        String propertyName = toPropertyName(needleMethodName);
        if (propertyName != null) {
            for (MethodDoc haystackMethod : cls.methods()) {
                String haystackMethodName = haystackMethod.name();
                if (needleMethodName.startsWith("set") && (haystackMethodName.equals("is" + propertyName) || haystackMethodName.equals("get" + propertyName))) {
                    return true;
                } else if ((needleMethodName.equals("is" + propertyName) || needleMethodName.equals("get" + propertyName)) && haystackMethodName.startsWith("set")) {
                    return true;
                }
            }
        }
        return false;
    }

    private String toPropertyName(String name) {
        if (name.startsWith("get") && name.length() > 3 && Character.isUpperCase(name.charAt(3))) {
            return name.substring(3);
        } else if (name.startsWith("is") && name.length() > 2 && Character.isUpperCase(name.charAt(2))) {
            return name.substring(2);
        } else if (name.startsWith("set") && name.length() > 3 && Character.isUpperCase(name.charAt(3))) {
            return name.substring(3);
        } else {
            return null;
        }
    }

    private void addInterfaces(ElementWrapper clsEl, ClassDoc cls) {
        ElementWrapper implementedInterfacesEl = clsEl.addChild("interfaces");
        do {
            for (ClassDoc implementedInterface : cls.interfaces()) {
                implementedInterfacesEl.addChild("interface", ATTR_Q_NAME, implementedInterface.qualifiedName());
            }
        } while (showInheritedInterfaces && (cls = cls.superclass()) != null);
    }

    private void addThrownExceptions(ElementWrapper methodsEl, MethodDoc m, RootDoc root) {
        ElementWrapper thrownExceptionsEl = methodsEl.addChild("exceptions");
        for (ClassDoc thrownException : m.thrownExceptions()) {
            ElementWrapper thrownExceptionEl = thrownExceptionsEl.addChild("exception", ATTR_Q_NAME, thrownException.qualifiedName());
            for (ThrowsTag throwsTag : m.throwsTags()) {
                if (throwsTag.exception().qualifiedName().equals(thrownException.qualifiedName())) {
                    addComment(thrownExceptionEl, throwsTag.inlineTags(), root);
                    break;
                }
            }
        }
    }

    private void addTypeInformation(ElementWrapper methodEl, Type type, String elementName) {
        ElementWrapper ownerEl = methodEl.addChild(elementName, "type", type.qualifiedTypeName());
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = type.asParameterizedType();

            for (Type typeArgument : parameterizedType.typeArguments()) {
                ownerEl.addChild("typeparameter", "type", typeArgument.qualifiedTypeName());
            }
        }
    }

    private void addParameters(ExecutableMemberDoc javadocItem, ElementWrapper parentEl) {
        ElementWrapper paramsEl = parentEl.addChild("parameters");
        for (Parameter parameter : javadocItem.parameters()) {
            addTypeInformation(paramsEl, parameter.type(), "parameter");
        }
    }

    private void addComment(ElementWrapper parentEl, Tag[] inlineTags, RootDoc root) {
        StringBuilder sb = new StringBuilder();

        tag:
        for (Tag tag : inlineTags) {
            for (TagHandler handler : tagHandlers) {
                if (handler.handles(tag)) {
                    sb.append(handler.toString(tag));
                    continue tag;
                }
            }
            sb.append(tag.text());
/*
            if (tag instanceof SeeTag) {
                SeeTag seeTag = (SeeTag) tag;
                String refClass = seeTag.referencedClass() != null ? seeTag.referencedClass().qualifiedName() : null;
                String refMember = seeTag.referencedMember() != null ? seeTag.referencedMember().name() : null;
                if (refClass != null) {
                    sb.append("<link class=\"").append(refClass).append('"');
                    if (refMember != null) {
                        sb.append(" member=\"").append(refMember).append('"');
                    }
                    sb.append(" />");
                }
            } else if ("@code".equals(tag.kind())) {
                sb.append("<code>").append(tag.text()).append("</code>");
            } else {
                sb.append(tag.text());
            }
*/
        }
        addComment(parentEl, sb.toString(), root);
    }

    private void addComment(ElementWrapper parentEl, String comment, RootDoc root) {
        String c = comment != null && comment.length() > 0 ? comment.replace("\n", "") : "";
        if (c.length() > 0) {
            try {
                StringBuilder sb = new StringBuilder();
                sb.append("<comment>");
                for (String paragraph : COMMENT_PARAGRAPH.split(c)) {
                    sb.append("<p>").append(paragraph).append("</p>");
                }
                sb.append("</comment>");
                parentEl.addChildFromSource(sb.toString());
            } catch (IOException e) {
                root.printWarning("Could not parse comment for " + parentEl.getTagName() + " " + parentEl.getAttribute(ATTR_NAME) + ". " + e.getMessage());
                parentEl.addChildWithText("comment", comment);
            } catch (SAXException e) {
                root.printWarning("Could not parse comment for " + parentEl.getTagName() + " " + parentEl.getAttribute(ATTR_NAME) + ". " + e.getMessage());
                parentEl.addChildWithText("comment", comment);
            }
        }
    }

    private void addAnnotations(ElementWrapper parentEl, ProgramElementDoc javadocItem) {
        for (AnnotationDesc annotation : javadocItem.annotations()) {
            parentEl.addChild("annotation", ATTR_Q_NAME, annotation.annotationType().qualifiedName());
        }
    }

    private String getAccess(ProgramElementDoc javadocItem) {
        if (javadocItem.isProtected()) {
            return "package";
        } else if (javadocItem.isPrivate()) {
            return "private";
        } else if (javadocItem.isPublic()) {
            return "public";
        } else {
            return "default";
        }
    }
}

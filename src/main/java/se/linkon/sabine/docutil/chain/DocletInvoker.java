package se.linkon.sabine.docutil.chain;

import com.sun.javadoc.DocErrorReporter;
import com.sun.javadoc.RootDoc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

class DocletInvoker {
    private Class<?> docletClass;
    private Method startMethod;
    private Method optionLengthMethod;
    private Method validOptionsMethod;

    private DocletInvoker(final Class<?> docletClass, final Method optionLengthMethod, final Method startMethod, final Method validOptionsMethod) {
        this.docletClass = docletClass;
        this.optionLengthMethod = optionLengthMethod;
        this.startMethod = startMethod;
        this.validOptionsMethod = validOptionsMethod;
    }

    public static DocletInvoker getInstance(String docletClassName) throws DocletWrapperException {
        try {
            Class<?> docletClass = Class.forName(docletClassName);
            Method startMethod = docletClass.getMethod("start", RootDoc.class);
            if (null == startMethod || !Modifier.isStatic(startMethod.getModifiers()) || !Modifier.isPublic(startMethod.getModifiers())) {
                throw new DocletWrapperException("Invalid Doclet. The start(RootDoc) method in " + docletClass.getName() + " is either missing or not marked public static.");
            }
            Method optionLengthMethod = docletClass.getMethod("optionLength", String.class);
            if (optionLengthMethod != null && (!Modifier.isStatic(startMethod.getModifiers()) || !Modifier.isPublic(startMethod.getModifiers()))) {
                throw new DocletWrapperException("Invalid Doclet. The optionLength(String) method in " + docletClass.getName() + " is not marked public static.");
            }
            Method validOptionsMethod = docletClass.getMethod("validOptions", String[][].class, DocErrorReporter.class);
            if (validOptionsMethod != null && (!Modifier.isStatic(validOptionsMethod.getModifiers()) || !Modifier.isPublic(validOptionsMethod.getModifiers()))) {
                throw new DocletWrapperException("Invalid Doclet. The validOptions(String[][], DocErrorReporter) method in " + docletClass.getName() + " is not marked public static.");
            }
            return new DocletInvoker(docletClass, optionLengthMethod, startMethod, validOptionsMethod);
        } catch (ExceptionInInitializerError e) {
            throw new DocletWrapperException(e);
        } catch (LinkageError e) {
            throw new DocletWrapperException(e);
        } catch (ClassNotFoundException e) {
            throw new DocletWrapperException(e);
        } catch (NoSuchMethodException e) {
            throw new DocletWrapperException(e);
        }
    }

    public String getDocletClassName() {
        return docletClass.getName();
    }

    public static class DocletWrapperException extends Exception {
        private DocletWrapperException(final Throwable cause) {
            super(cause);
        }

        private DocletWrapperException(final String message) {
            super(message);
        }
    }

    public boolean start(RootDoc root) {
        try {
            if (null != startMethod) {
                return (Boolean) startMethod.invoke(null, root);
            }
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        }
        return false;
    }

    public int optionLength(String option) {
        try {
            if (null != optionLengthMethod) {
                return (Integer) optionLengthMethod.invoke(null, option);
            }
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        }
        return 0;
    }

    public boolean validOptions(String options[][], DocErrorReporter reporter) {
        try {
            if (null != validOptionsMethod) {
                return (Boolean) validOptionsMethod.invoke(null, options, reporter);
            }
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        }
        return true;
    }
}

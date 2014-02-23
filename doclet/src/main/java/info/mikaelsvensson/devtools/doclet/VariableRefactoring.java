/*
 * Copyright 2014 Mikael Svensson
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package info.mikaelsvensson.devtools.doclet;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.*;
import japa.parser.ast.expr.*;
import japa.parser.ast.stmt.ExpressionStmt;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

//TODO Remove class
public class VariableRefactoring {
    public static void main (String[] args) {
        try {
            String path = "/home/mikael/Development/doclet/src/main/java/info/mikaelsvensson/devtools/VariableRefactoringTest.java";
            CompilationUnit compilationUnit = JavaParser.parse(new File(path));
            Map<String, List<String>> privateFields = new HashMap<String, List<String>>();

            for (TypeDeclaration typeDeclaration : compilationUnit.getTypes()) {
                if (!privateFields.containsKey(typeDeclaration)) {
                    privateFields.put(typeDeclaration.getName(), new LinkedList<String>());
                }
                for (BodyDeclaration bodyDeclaration : typeDeclaration.getMembers()) {
                    if (bodyDeclaration instanceof FieldDeclaration) {
                        FieldDeclaration fieldDeclaration = (FieldDeclaration) bodyDeclaration;
                        for (VariableDeclarator variableDeclarator : fieldDeclaration.getVariables()) {
                            if (ModifierSet.isPrivate(fieldDeclaration.getModifiers())) {
                                privateFields.get(typeDeclaration.getName()).add(variableDeclarator.getId().getName());
                            }
                            System.out.println(variableDeclarator.getId().getName());
                        }
                    }
                }
            }

            new VariableRefactoringVisitor(privateFields).visit(compilationUnit, null);

            System.out.println(compilationUnit.toString());
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    static class VariableRefactoringVisitor extends VoidVisitorAdapter {
        private Map<String, List<String>> privateFields;
        private TypeDeclaration currentType;
        private String currentTypeName;

        public VariableRefactoringVisitor(Map<String, List<String>> privateFields) {
            this.privateFields = privateFields;
        }

        @Override
        public void visit(ClassOrInterfaceDeclaration n, Object arg) {
            this.currentTypeName = n.getName();
            super.visit(n, arg);    //To change body of overridden methods use File | Settings | File Templates.
        }

        @Override
        public void visit(AssignExpr n, Object arg) {
            System.out.println("Assigning to " + n.getTarget());
            super.visit(n, arg);    //To change body of overridden methods use File | Settings | File Templates.
        }


        @Override
        public void visit(ExpressionStmt n, Object arg) {
            System.out.println("Expression " + n.getExpression().toString());
            if (n.getExpression() instanceof FieldAccessExpr) {
                System.out.println("Accessing " + ((FieldAccessExpr) n.getExpression()).getField());
            }
            super.visit(n, arg);    //To change body of overridden methods use File | Settings | File Templates.
        }

        @Override
        public void visit(FieldAccessExpr n, Object arg) {
            System.out.println("FieldAccess " + n.getField());
            System.out.println("scope of field: " + n.getScope().toString());
            super.visit(n, arg);    //To change body of overridden methods use File | Settings | File Templates.
        }

        @Override
        public void visit(FieldDeclaration n, Object arg) {
            System.out.println("Declaring field of type " + n.getType().getClass().getName());
            super.visit(n, arg);    //To change body of overridden methods use File | Settings | File Templates.
        }

        @Override
        public void visit(InitializerDeclaration n, Object arg) {
            super.visit(n, arg);    //To change body of overridden methods use File | Settings | File Templates.
        }

        @Override
        public void visit(MemberValuePair n, Object arg) {
            System.out.println("MemberValuePair " + n.getName() + ", " + n.getValue());
            super.visit(n, arg);    //To change body of overridden methods use File | Settings | File Templates.
        }

        @Override
        public void visit(NameExpr n, Object arg) {
            boolean isProbablyPrivate = false;
            if (currentTypeName != null) {
                if (privateFields.containsKey(currentTypeName)) {
                    isProbablyPrivate = privateFields.get(currentTypeName).contains(n.getName());
                }
            }
            System.out.println("NameExpr " + n.getName());
            if (isProbablyPrivate && !n.getName().startsWith("_")) {
                n.setName("_" + n.getName());
            }
            super.visit(n, arg);    //To change body of overridden methods use File | Settings | File Templates.
        }

        @Override
        public void visit(ThisExpr n, Object arg) {
            System.out.println("this = " + n.getClassExpr());
            super.visit(n, arg);    //To change body of overridden methods use File | Settings | File Templates.
        }

        @Override
        public void visit(VariableDeclarationExpr n, Object arg) {
            System.out.println("Declaring " + n.getVars().size() + " of something");
            super.visit(n, arg);    //To change body of overridden methods use File | Settings | File Templates.
        }

        @Override
        public void visit(VariableDeclarator n, Object arg) {
            System.out.println("VariableDeclarator " + n.getId().getName());
            super.visit(n, arg);    //To change body of overridden methods use File | Settings | File Templates.
        }

        @Override
        public void visit(VariableDeclaratorId n, Object arg) {
            super.visit(n, arg);    //To change body of overridden methods use File | Settings | File Templates.
        }
    }

}

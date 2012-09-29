/*
 * Copyright (c) 2012, Mikael Svensson
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the copyright holder nor the names of the
 *       contributors of this software may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL MIKAEL SVENSSON BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package info.mikaelsvensson.doctools.doclet.chain;

import com.sun.javadoc.DocErrorReporter;
import com.sun.javadoc.RootDoc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class DocletInvoker {
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
            Method validOptionsMethod = null;
            try {
                validOptionsMethod = docletClass.getMethod("validOptions", String[][].class, DocErrorReporter.class);
                if (validOptionsMethod != null && (!Modifier.isStatic(validOptionsMethod.getModifiers()) || !Modifier.isPublic(validOptionsMethod.getModifiers()))) {
                    throw new DocletWrapperException("Invalid Doclet. The validOptions(String[][], DocErrorReporter) method in " + docletClass.getName() + " is not marked public static.");
                }
            } catch (NoSuchMethodException e) {
                validOptionsMethod = null;
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

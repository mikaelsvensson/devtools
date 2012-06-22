package se.linkon.sabine.docutil.chain;

import com.sun.javadoc.RootDoc;

public class CustomOptionsRootDoc extends DocRootWrapper {
    private String[][] options;

    public CustomOptionsRootDoc(final RootDoc root, String[][] options) {
        super(root);
        this.options = options;
    }

    @Override
    public String[][] options() {
        return options;
    }
}

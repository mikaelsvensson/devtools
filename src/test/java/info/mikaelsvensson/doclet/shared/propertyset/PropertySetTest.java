package info.mikaelsvensson.doclet.shared.propertyset;

import org.junit.Test;

import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class PropertySetTest {
    @Test
    public void testName() throws Exception {
        PropertySet ps = new PropertySet();
        ps.setProperty("familyName", "Johnson");
        ps.setProperty("familyMember.alice.nickName", "alice");
        ps.setProperty("familyMember.alice.emailAddress", "alice@mail.com");
        ps.setProperty("familyMember.robert.nickName", "robby");
        ps.setProperty("familyMember.robert.emailAddress", "robert@mail.com");
        ps.setProperty("familyMember.sally.nickName", "sal");
        ps.setProperty("familyMember.sally.emailAddress", "sally@mail.com");

        assertThat(ps.getProperty("familyName"), is("Johnson"));
        assertThat(ps.getProperty("familyMember.alice.nickName"), is("alice"));
        assertThat(ps.getProperty("familyMember.alice.emailAddress"), is("alice@mail.com"));
        assertThat(ps.getProperty("familyMember.robert.nickName"), is("robby"));
        assertThat(ps.getProperty("familyMember.robert.emailAddress"), is("robert@mail.com"));
        assertThat(ps.getProperty("familyMember.sally.nickName"), is("sal"));
        assertThat(ps.getProperty("familyMember.sally.emailAddress"), is("sally@mail.com"));

        assertThat(ps.getCollection("familyMember").size(), is(3));
        assertThat(ps.getCollection("familyMember").get("alice").getProperty("nickName"), is("alice"));
        for (Map.Entry<String, PropertySet> entry : ps.getCollection("familyMember").entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        System.out.println(ps);
    }
}

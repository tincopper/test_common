package com.tomgs.test.cli;


import static java.util.Arrays.*;
import static java.util.Collections.*;

import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;
import static org.junit.rules.ExpectedException.none;

/**
 * @author tangzhongyuan
 * @create 2018-11-29 11:54
 **/
public class ShortOptionsWithArgumentsTest {

    @Test
    public void allowsOptionsToAcceptArguments() {
        OptionParser parser = new OptionParser("fc:q::");
        OptionSet options = parser.parse("-f", "-c", "foo", "-q");
        assertTrue(options.has("f"));
        assertTrue(options.has("c"));
        assertTrue(options.hasArgument("c"));
        assertEquals("foo", options.valueOf("c"));
        assertEquals(asList("foo"), options.valuesOf("c"));
        assertTrue(options.has("q"));
        assertFalse(options.hasArgument("q"));
        assertNull(options.valueOf("q"));
        assertEquals(emptyList(), options.valuesOf("q"));
    }

    @Test
    public void allowsDifferentFormsOfPairingArgumentWithOption() {
        OptionParser parser = new OptionParser("a:b:c::");
        OptionSet options = parser.parse("-a", "foo", "-bbar", "-c=baz");
        assertTrue(options.has("a"));
        assertTrue(options.hasArgument("a"));
        assertEquals("foo", options.valueOf("a"));
        assertTrue(options.has("b"));
        assertTrue(options.hasArgument("b"));
        assertEquals("bar", options.valueOf("b"));
        assertTrue(options.has("c"));
        assertTrue(options.hasArgument("c"));
        assertEquals("baz", options.valueOf("c"));
    }


    @Rule
    public final ExpectedException thrown = none();

    @Test
    public void allowsMultipleValuesForAnOption() {
        OptionParser parser = new OptionParser("a:");
        OptionSet options = parser.parse("-a", "foo", "-abar", "-a=baz");
        assertTrue(options.has("a"));
        assertTrue(options.hasArgument("a"));
        assertEquals(asList("foo", "bar", "baz"), options.valuesOf("a"));
        thrown.expect(OptionException.class);
        options.valueOf("a");
    }


    @Test
    public void allowsClusteringShortOptionsThatDoNotAcceptArguments() {
        OptionParser parser = new OptionParser("aBcd");
        OptionSet options = parser.parse("-cdBa");
        assertTrue(options.has("a"));
        assertTrue(options.has("B"));
        assertTrue(options.has("c"));
        assertTrue(options.has("d"));
    }

    @Test
    public void acceptsLongOptions() {
        OptionParser parser = new OptionParser();
        parser.accepts("flag");
        parser.accepts("verbose");
        OptionSet options = parser.parse("--flag");
        assertTrue(options.has("flag"));
        assertFalse(options.has("verbose"));
    }
}
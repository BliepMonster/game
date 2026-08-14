package main;

import saving.SavedAttributeData;

import java.util.Arrays;
import java.util.HashSet;

public class AttributeData {
    private final HashSet<String> attributes = new HashSet<>();
    public AttributeData(SavedAttributeData data) {
        attributes.addAll(Arrays.stream(data.attribs()).toList());
    }
    public void add(String attrib) {
        attributes.add(attrib);
    }
    public boolean contains(String attrib) {
        return attributes.contains(attrib);
    }
    public SavedAttributeData save() {
        return new SavedAttributeData(attributes.toArray(new String[0]));
    }
}

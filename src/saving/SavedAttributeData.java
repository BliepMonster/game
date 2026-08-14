package saving;

import engine.saving.objects.SavedArray;
import engine.saving.objects.SavedObject;
import engine.saving.objects.SavedString;

import java.util.ArrayList;
import java.util.Arrays;

public record SavedAttributeData(String[] attribs) {
    public SavedObject save() {
        return new SavedArray(new ArrayList<>(Arrays.stream(attribs).map(SavedString::new).toList()));
    }
}

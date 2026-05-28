package saving;

import engine.saving.objects.SavedArray;
import engine.saving.objects.SavedObject;

import java.util.ArrayList;

public record SavedSkillSet(SavedSkill[] skills) {
    public SavedSkillSet(SavedObject object) {
        SavedArray array = (SavedArray) object;
        SavedSkill[] skills = new SavedSkill[array.length()];
        for (int i = 0; i < skills.length; ++i) {
            skills[i] = new SavedSkill(array.get(i));
        }
        this(skills);
    }
    public SavedObject save() {
        ArrayList<SavedObject> objects = new ArrayList<>();
        for (var skill : skills) {
            objects.add(skill.save());
        }
        return new SavedArray(objects);
    }
}

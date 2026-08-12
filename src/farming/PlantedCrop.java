package farming;

// compost growth buff already applied to endTime
public record PlantedCrop(CropType type, long endTime, CompostType compost) {
}

package Repositories.FromDB;

import Objects.PickflowmeterWear;

public final class PickflowmeterWearRepository {
    private final PickflowmeterWear pickflowmeterWear;

    public PickflowmeterWearRepository(PickflowmeterWear pickflowmeterWear) {
        this.pickflowmeterWear = pickflowmeterWear;
    }

    public PickflowmeterWear getPickflowmeterWear() {
        return pickflowmeterWear;
    }
}

package enumResources;

public class ResourcesHelper {

    public enum testResultsEnum {

        BRONZE(1),
        SILVER(2),
        GOLD(3);

        private int value;

        testResultsEnum(int value) {
            this.value = value;
        }
    }

}

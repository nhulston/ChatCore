package me.cranked.chatcore;

import org.bukkit.Bukkit;

public class VersionManager {
    private static final boolean[] versions = new boolean[14];

    /**
     * Initialize the all the versions in a boolean array
     */
    public static void initVersions() {
        String version = Bukkit.getVersion();
        if (version.contains("1.8")) {
            setVersion(1);
        } else if (version.contains("1.9")) {
            setVersion(2);
        } else if (version.contains("1.10")) {
            setVersion(3);
        } else if (version.contains("1.11")) {
            setVersion(4);
        } else if (version.contains("1.12")) {
            setVersion(5);
        } else if (version.contains("1.13")) {
            setVersion(6);
        } else if (version.contains("1.14")) {
            setVersion(7);
        } else if (version.contains("1.15")) {
            setVersion(8);
        } else if (version.contains("1.16")) {
            setVersion(9);
        } else if (version.contains("1.17")) {
            setVersion(10);
        } else if (version.contains("1.18")) {
            setVersion(11);
        } else if (version.contains("1.19")) {
            setVersion(12);
        } else if (version.contains("1.20")) {
            setVersion(13);
        }
    }

    /**
     * @return true if the version is at least 1.8
     */
    public static boolean isV8() {
        return versions[1];
    }

    /**
     * @return true if the version is at least 1.9
     */
    public static boolean isV9() {
        return versions[2];
    }

    /**
     * @return true if the version is at least 1.10
     */
    public static boolean isV10() {
        return versions[3];
    }

    /**
     * @return true if the version is at least 1.11
     */
    public static boolean isV11() {
        return versions[4];
    }

    /**
     * @return true if the version is at least 1.12
     */
    public static boolean isV12() {
        return versions[5];
    }

    /**
     * @return true if the version is at least 1.13
     */
    public static boolean isV13() {
        return versions[6];
    }

    /**
     * @return true if the version is at least 1.14
     */
    public static boolean isV14() {
        return versions[7];
    }

    /**
     * @return true if the version is at least 1.15
     */
    public static boolean isV15() {
        return versions[8];
    }

    /**
     * @return true if the version is at least 1.16
     */
    public static boolean isV16() {
        return versions[9];
    }

    /**
     * @return true if the version is at least 1.17
     */
    public static boolean isV17() {
        return versions[10];
    }

    /**
     * @return true if the version is at least 1.18
     */
    public static boolean isV18() {
        return versions[11];
    }

    /**
     * @return true if the version is at least 1.19
     */
    public static boolean isV19() {
        return versions[12];
    }

    /**
     * @return true if the version is at least 1.20
     */
    public static boolean isV20() {
        return versions[13];
    }

    /**
     * Helper method for initVersions
     * @param index The index of the boolean to set true and all indices under it
     */
    private static void setVersion(int index) {
        for (int i = index; i >= 0; i--) {
            versions[i] = true;
        }
    }
}

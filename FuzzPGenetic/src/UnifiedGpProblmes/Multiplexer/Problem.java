package UnifiedGpProblmes.Multiplexer;

import java.util.function.Function;

public class Problem {
//@formatter:off
  static final int[] probes = new int[]{1026, 2, 1027, 1029, 6, 1031, 1034, 12, 13, 17, 21, 23, 24, 25, 26, 1050, 27, 1051, 28, 1052, 29, 1053, 1054, 32, 1058, 36, 39, 1065, 41, 42, 45, 1070, 1071, 47, 48, 1072, 1073, 50, 53, 1077, 1078, 1079, 59, 1087, 65, 1091, 68, 70, 1096, 1098, 1100, 1104, 83, 85, 87, 90, 95, 1119, 1120, 1124, 100, 103, 1132, 1133, 110, 1134, 1138, 1139, 1140, 117, 118, 119, 123, 1148, 1151, 127, 1157, 1161, 1164, 140, 1165, 142, 145, 1170, 146, 1171, 1176, 1181, 157, 1184, 160, 1186, 163, 1190, 1191, 1192, 169, 173, 174, 1201, 178, 1203, 179, 180, 181, 1207, 1208, 187, 188, 191, 196, 1221, 1222, 199, 201, 204, 206, 1233, 213, 214, 215, 216, 1240, 217, 1242, 1243, 1245, 1249, 226, 1251, 227, 1254, 1256, 1259, 237, 1262, 239, 1265, 1266, 242, 243, 1267, 1269, 1272, 250, 251, 1277, 1279, 255, 1282, 1283, 260, 1284, 1285, 263, 1287, 264, 1289, 1290, 267, 1291, 1292, 269, 271, 272, 273, 1298, 1301, 282, 284, 287, 289, 1315, 291, 292, 1316, 1317, 293, 294, 1318, 1320, 297, 1323, 299, 1324, 1325, 301, 1327, 303, 304, 1330, 306, 1335, 312, 1337, 313, 319, 1344, 322, 323, 1347, 1350, 1352, 334, 340, 341, 343, 347, 1372, 349, 1373, 351, 1377, 1378, 354, 355, 1382, 360, 1385, 1386, 364, 1390, 366, 1391, 367, 370, 1395, 1398, 374, 1400, 376, 377, 1403, 1404, 382, 1407, 1408, 1409, 386, 388, 1412, 390, 1416, 392, 1417, 1418, 397, 1422, 400, 403, 1427, 1430, 1431, 411, 1436, 416, 418, 1442, 1443, 1446, 1447, 1448, 429, 430, 431, 1456, 1457, 434, 1458, 1460, 1461, 437, 1466, 442, 444, 445, 1471, 1472, 452, 1477, 453, 1478, 455, 1480, 1482, 458, 1485, 462, 464, 465, 1489, 467, 470, 1497, 474, 1498, 476, 1500, 1503, 480, 483, 1508, 484, 487, 1513, 489, 1517, 495, 496, 1523, 1525, 1527, 1529, 510, 512, 1537, 1538, 514, 1539, 515, 1547, 528, 530, 531, 532, 533, 535, 1562, 541, 542, 1567, 1568, 551, 1575, 1580, 556, 558, 1582, 1584, 561, 563, 1588, 564, 1590, 1592, 568, 570, 574, 1598, 1599, 1601, 1602, 1603, 579, 1604, 1605, 1606, 1608, 584, 587, 590, 1615, 592, 1617, 1618, 596, 597, 600, 1626, 604, 1629, 1630, 1631, 1632, 608, 1634, 611, 1635, 1637, 613, 1640, 618, 1643, 1646, 1648, 1650, 1656, 633, 1657, 634, 1660, 1664, 641, 1665, 1667, 1668, 1672, 1673, 652, 1680, 656, 1683, 660, 662, 1688, 1689, 1691, 1692, 668, 1694, 1695, 672, 1697, 674, 1700, 1702, 680, 1705, 1707, 1708, 1709, 687, 1711, 1714, 690, 1715, 1719, 699, 702, 1728, 1730, 707, 1732, 708, 711, 1743, 1744, 720, 721, 722, 1748, 726, 1753, 1754, 732, 1757, 1759, 738, 740, 1765, 741, 742, 1769, 746, 752, 1776, 754, 1779, 756, 1780, 758, 760, 761, 1787, 763, 766, 1791, 767, 768, 769, 1794, 1799, 1800, 777, 778, 1802, 1805, 782, 783, 785, 1810, 1811, 1812, 788, 1814, 791, 1816, 795, 796, 1821, 1822, 800, 1825, 1826, 1827, 1833, 810, 1835, 1838, 815, 816, 817, 1842, 823, 1848, 1849, 1856, 1859, 836, 839, 1864, 840, 1866, 848, 851, 852, 1881, 858, 1882, 860, 1887, 1888, 864, 866, 1893, 870, 872, 1897, 1898, 874, 876, 1900, 877, 879, 1903, 1904, 883, 1907, 888, 1917, 1921, 1922, 1924, 1926, 1928, 907, 909, 1934, 1937, 1938, 1941, 1945, 925, 1952, 931, 1958, 1959, 939, 1966, 1967, 1969, 1971, 949, 952, 1977, 953, 955, 1983, 959, 962, 963, 964, 965, 966, 976, 977, 980, 2006, 983, 2007, 2009, 987, 2011, 990, 2022, 1000, 1001, 2027, 1003, 1009, 1012, 2039, 2040, 2041, 1017, 1019, 2043, 2044, 1022, 1023};
//@formatter:on

  public int hits(Function<boolean[], Boolean> what) {
    int hits = 0;
    for (int i : probes) {
      boolean[] bits = intToBit(i);
      Boolean rez = what.apply(bits);
      if (rez != null) {
        int index = 4 * ((bits[10] ? 1 : 0)) + 2 * (bits[9] ? 1 : 0) + (bits[8] ? 1 : 0);
        boolean theory = bits[index];
        if (rez.booleanValue() == theory) {
          hits += 1;
        }
      }

    }
    return hits;

  }

  public int fullHits(Function<boolean[], Boolean> what) {
    int hits = 0;
    for (int i = 0; i < 1 << 11; i++) {
      boolean[] bits = intToBit(i);
      Boolean rez = what.apply(bits);
      if (rez != null) {
        int index = 4 * ((bits[10] ? 1 : 0)) + 2 * (bits[9] ? 1 : 0) + (bits[8] ? 1 : 0);
        boolean theory = bits[index];
        if (rez.booleanValue() == theory) {
          hits += 1;
        }
      }

    }
    return hits;

  }

  private static boolean[] intToBit(int inp) {
    boolean[] toRet = new boolean[11];
    for (int i = 0; i < 11; i++) {
      toRet[i] = (inp & (1 << i)) != 0;
    }
    return toRet;
  }

}

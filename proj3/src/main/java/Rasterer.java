import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {
    /** The max depth of the map */
    private static final int MAX_DEPTH = 7;

    /** The LonDPP of depth 0 tile */
    private static final double DEPTH_ZERO_LONDPP = 0.00034332275390625;

    private static final Point<Double> ROOT_UL = new Point<>(MapServer.ROOT_ULLON, MapServer.ROOT_ULLAT);
    private static final Point<Double> ROOT_LR = new Point<>(MapServer.ROOT_LRLON, MapServer.ROOT_LRLAT);

    private static final double[] LENGTH_OF_DEPTH_I_IMAGE = new double[8];
    private static final double[] WIDTH_OF_DEPTH_I_IMAGE = new double[8];

    /** The class is for the representation of coordinates */
    private static final class Point<T extends Number> {

        private final T x;
        private final T y;

        Point(T x, T y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "x = " + x.toString() + "  y = " + y.toString();
        }

    }

    private Point<Double> ul;
    private Point<Double> lr;

    private int depth;

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        if (!isValidRequest(params)) {
            return badQueryResult();
        }
        analyseRequest(params);
        Map<String, Object> results = getResults();

        return results;
    }

    /** Initialize the Rasterer, calculate the length and width table in advance. */
    public Rasterer() {
        double width = MapServer.ROOT_LRLON - MapServer.ROOT_ULLON;
        double length = MapServer.ROOT_ULLAT - MapServer.ROOT_LRLAT;
        for (int i = 0; i <= MAX_DEPTH; i += 1) {
            LENGTH_OF_DEPTH_I_IMAGE[i] = length;
            WIDTH_OF_DEPTH_I_IMAGE[i] = width;
            length /= 2;
            width /= 2;
        }
    }

    /** Get the information from params */
    private void analyseRequest(Map<String, Double> params) {

        ul = new Point<>(params.get("ullon"), params.get("ullat"));
        lr = new Point<>(params.get("lrlon"), params.get("lrlat"));

        double w = params.get("w");
        depth = calcDepth(calcLonDPP(lr.x, ul.x, w));

    }

    private boolean isValidRequest(Map<String, Double> params) {
        if (params.get("ullon") > params.get("lrlon") ||
                params.get("lrlat") > params.get("ullat")) {
            return false;
        }
        if (params.get("ullon") > MapServer.ROOT_LRLON ||
                params.get("ullat") < MapServer.ROOT_LRLAT ||
                params.get("lrlon") < MapServer.ROOT_ULLON ||
                params.get("lrlat") > MapServer.ROOT_ULLAT) {
            return false;
        }
        return true;
    }

    private static double calcLonDPP(double lrlon, double ullon, double width) {
        return Math.abs(lrlon - ullon) / width;
    }

    private static int calcDepth(double LonDPP) {
        double tmpLonDPP = DEPTH_ZERO_LONDPP;
        int depth = MAX_DEPTH;
        for (int i = 0; i <= MAX_DEPTH; i += 1) {
            if (tmpLonDPP < LonDPP) {
                depth = i;
                break;
            }
            tmpLonDPP /= 2;
        }
        return depth;
    }

    /** Get all the results and put them into a map */
    private Map<String, Object> getResults() {
        Point<Integer> ulFileXY = getFileXY(ul, depth);
        Point<Integer> lrFileXY = getFileXY(lr, depth);

        Point<Double> ulRasterPos = calcFilePosition(ulFileXY, depth, true);
        Point<Double> lrRasterPos = calcFilePosition(lrFileXY, depth, false);

        Map<String, Object> results = new HashMap<>();

        results.put("raster_ul_lon", ulRasterPos.x);
        results.put("raster_ul_lat", ulRasterPos.y);
        results.put("raster_lr_lon", lrRasterPos.x);
        results.put("raster_lr_lat", lrRasterPos.y);

        String[][] renderGrid = new String[lrFileXY.y - ulFileXY.y + 1][lrFileXY.x - ulFileXY.x + 1];
        int x = 0;
        int y = 0;
        for (int i = ulFileXY.y; i <= lrFileXY.y; i += 1) {
            for (int j = ulFileXY.x; j <= lrFileXY.x; j += 1) {
                renderGrid[x][y] = "d" + depth + "_x" + j + "_y" + i + ".png";
                y += 1;
            }
            x += 1;
            y = 0;
        }

        results.put("render_grid", renderGrid);
        results.put("depth", depth);
        results.put("query_success", true);

        return results;
    }

    /**
     * Get the filename that the image contains the given position in the given depth.
     * @return the x and y in the filename.
     */
    public static Point<Integer> getFileXY(Point<Double> pos, int depth) {
        return getFileXYHelper(ROOT_UL, ROOT_LR, pos, (int) Math.pow(2, depth - 1));
    }

    /**
     * Search the corresponding image index (x and y) of the given position. (like a QuadTree?)
     * @param scale the scale of current recursion search.
     * @return Point<Integer> represents the x and y.
     */
    private static Point<Integer> getFileXYHelper(Point<Double> ul, Point<Double> lr, Point<Double> pos, int scale) {
        double ulx = ul.x, lrx = lr.x, uly = ul.y, lry = lr.y;
        Point<Double> mid = new Point<>((ulx + lrx) / 2, (uly + lry) / 2);

        if (scale == 0) {
            return new Point<>(0, 0);
        }
        int resx = 0, resy = 0;
        if (pos.y < mid.y) {
            resy = 1;
        }
        if (pos.x > mid.x) {
            resx = 1;
        }

        double halfWidth = (lrx - ulx) / 2;
        double halfHeight = (uly - lry) / 2;

        Point<Double> nextAreaUL = new Point<>(ulx + halfWidth * resx, uly - halfHeight * resy);
        Point<Double> nextAreaLR = new Point<>(lrx - halfWidth * (1 - resx), lry + halfHeight * (1 - resy));
        Point<Integer> next = getFileXYHelper(nextAreaUL, nextAreaLR, pos, scale / 2);

        return new Point<>(resx * scale + next.x, resy * scale + next.y);
    }

    /**
     * Calculate the ullat, ullon, lrlat, lrlon of the given file.
     * @param fileXY the x, y in the filename, for example 1, 0 for "d2_x1_y0" .
     * @param isUL true for ul of the file, false for lr of the file.
     * @return an array of double contains ullat, ullon, lrlat, lrlon in order.
     */
    private static Point<Double> calcFilePosition(Point<Integer> fileXY, int depth, boolean isUL) {
        double y = ROOT_UL.y - LENGTH_OF_DEPTH_I_IMAGE[depth] * fileXY.y;
        double x = ROOT_UL.x + WIDTH_OF_DEPTH_I_IMAGE[depth] * fileXY.x;
        if (isUL) {
            return new Point<>(x, y);
        }
        return new Point<>(x + WIDTH_OF_DEPTH_I_IMAGE[depth], y - LENGTH_OF_DEPTH_I_IMAGE[depth]);
    }

    private static Map<String, Object> badQueryResult() {
        Map<String, Object> results = new HashMap<>();
        results.put("raster_ul_lon", 0);
        results.put("raster_ul_lat", 0);
        results.put("raster_lr_lon", 0);
        results.put("raster_lr_lat", 0);
        results.put("render_grid", null);
        results.put("depth", 0);
        results.put("query_success", false);
        return results;
    }

}

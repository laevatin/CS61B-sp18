public class NBody {

    public static double readRadius(String path) {
        In in = new In(path);

        in.readInt();
        return in.readDouble();
    }

    public static Planet[] readPlanets(String path) {
        In in = new In(path);

        int numOfPlanets = in.readInt();
        Planet[] planets = new Planet[numOfPlanets];
        in.readDouble();

        for (int i = 0; i < numOfPlanets; i++) {
            planets[i] = new Planet(in.readDouble(), in.readDouble(), in.readDouble(), in.readDouble(), in.readDouble(), in.readString());
        }

        return planets;
    }

    public static void main(String[] args) {

        double T = Double.parseDouble(args[0]);
        double dT = Double.parseDouble(args[1]);
        String filename = args[2];
        double radius = readRadius(filename);
        Planet[] planets = readPlanets(filename);
        int numOfPlanets = planets.length;
        String background = "images/starfield.jpg";

        StdDraw.enableDoubleBuffering();
        StdDraw.setScale(-radius, radius);
        
        Double time = 0.0;
        double[] xForces = new double[numOfPlanets];
        double[] yForces = new double[numOfPlanets];

        while (time < T) {

            for (int i = 0; i < numOfPlanets; i++) {
                xForces[i] = planets[i].calcNetForceExertedByX(planets);
                yForces[i] = planets[i].calcNetForceExertedByY(planets);
            }

            for (int i = 0; i < numOfPlanets; i++) {
                planets[i].update(dT, xForces[i], yForces[i]);
            }

            StdDraw.picture(0, 0, background, 2 * radius, 2 * radius);
            for (Planet p : planets) {
                p.draw();
            }
            StdDraw.show();
            StdDraw.pause(10);

            time += dT;
        }
        
        StdOut.printf("%d\n", planets.length);
        StdOut.printf("%.2e\n", radius);
        for (int i = 0; i < planets.length; i++) {
            StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                planets[i].xxPos, planets[i].yyPos, planets[i].xxVel,
                planets[i].yyVel, planets[i].mass, planets[i].imgFileName);   
        }

    }

}
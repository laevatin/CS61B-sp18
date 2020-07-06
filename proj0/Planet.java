public class Planet {

    public double xxPos;
    public double yyPos;
    public double xxVel;
    public double yyVel;
    public double mass;
    public String imgFileName;


    public Planet(double xP, double yP, double xV,
                double yV, double m, String img) {
        xxPos = xP;
        yyPos = yP;
        xxVel = xV;
        yyVel = yV;
        mass = m;
        imgFileName = new String(img);
    }

    public Planet(Planet p) {
        xxPos = p.xxPos;
        yyPos = p.yyPos;
        xxVel = p.xxVel;
        yyVel = p.yyVel;
        mass = p.mass;
        imgFileName = new String(p.imgFileName);
    }

    public double calcDistance(Planet p) {
        return Math.sqrt(Math.pow(p.xxPos - xxPos, 2) + Math.pow(p.yyPos - yyPos, 2));
    }

    public double calcForceExertedBy(Planet p) {
        double G = 6.67e-11;
        double r = calcDistance(p);
        return G * mass * p.mass / Math.pow(r, 2);
    }

    public double calcForceExertedByX(Planet p) {
        return calcForceExertedBy(p) * (p.xxPos - xxPos) / calcDistance(p);
    }

    public double calcForceExertedByY(Planet p) {
        return calcForceExertedBy(p) * (p.yyPos - yyPos) / calcDistance(p);
    }

    public double calcNetForceExertedByX(Planet[] parray) {
        double netForceX = 0;
        for (Planet p : parray) {
            if (p != this){
                netForceX += calcForceExertedByX(p);
            }
        }
        return netForceX;
    }

    public double calcNetForceExertedByY(Planet[] parray) {
        double netForceY = 0;
        for (Planet p : parray) {
            if (p != this){
                netForceY += calcForceExertedByY(p);
            }
        }
        return netForceY;
    }

    public void update(double dt, double xForce, double yForce) {
        double ax = xForce / mass;
        double ay = yForce / mass;
        xxVel = xxVel + ax * dt;
        yyVel = yyVel + ay * dt;
        xxPos = xxPos + xxVel * dt;
        yyPos = yyPos + yyVel * dt;
    }

    public void draw() {
        StdDraw.picture(xxPos, yyPos, "images/" + imgFileName);
    }

}
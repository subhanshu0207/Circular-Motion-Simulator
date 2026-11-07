package physics;
import utils.*;

public class Physics {

    public double pathRadius, ballRadius;
    public double initTheta, initOmega;
    public double omega, alpha, theta;
    public double timeScale;
    public double x, y;    //co-ordinates of center of ball(in meters)

    private final double maxOmega = 5;

    public Vector at;  //Tangential acceleration
    public Vector ac;  //Centripetal acceleration
    public Vector v;   //Velocity
    public Vector acc;   //Resultant Vector

    public CustomTime time;
    public Physics(double pathRadius, double ballRadius) {   //in meters
        time = new CustomTime();
        this.pathRadius = pathRadius;
        this.ballRadius = ballRadius;

        reset();
    }

    public void reset() {
        timeScale = 1.0;
        updateTimeScale(timeScale);
        omega = 0;
        alpha = 0;
        initTheta = 0;
        theta = initTheta;
        initOmega = 0;
        x = pathRadius * Math.cos(initTheta);
        y = pathRadius * Math.sin(initTheta);

        at = new Vector(pathRadius * alpha, initTheta + Math.toRadians(90));
        ac = new Vector(pathRadius * initOmega * initOmega, initTheta + Math.toRadians(180));
        v = new Vector(pathRadius * initOmega, initTheta + Math.toRadians(90));
        acc = new Vector(Math.hypot(ac.magnitude, at.magnitude), initTheta + Math.atan2(at.magnitude, ac.magnitude));
    }

    public void update() {
        time.update();

        double dt = time.getDeltaTime();

        omega += alpha * dt;
        omega = Math.max(-maxOmega, Math.min(omega, maxOmega));
        theta += omega * dt;
        theta %= 2 * Math.PI;

        updatePosition();
        updateVectors();
    }

    public void updatePosition() {
        x = pathRadius * Math.cos(theta);
        y = pathRadius * Math.sin(theta);
    }

    public void updateVectors() {
        at.magnitude = 15 * pathRadius * Math.abs(alpha);
        at.angle = theta + ((alpha > 0) ? Math.PI / 2 : -Math.PI / 2);

        ac.magnitude = pathRadius * omega * omega;
        ac.angle = theta + Math.PI;

        v.magnitude = 3 * pathRadius * Math.abs(omega);
        v.angle = theta + ((omega > 0) ? Math.PI / 2 : -Math.PI / 2);

        acc.magnitude = Math.hypot(at.magnitude, ac.magnitude);

        double ax = ac.magnitude * Math.cos(ac.angle) + at.magnitude * Math.cos(at.angle);
        double ay = ac.magnitude * Math.sin(ac.angle) + at.magnitude * Math.sin(at.angle);

        acc.angle = Math.atan2(ay, ax);
    }

    public void updateTimeScale(double timeScale) {
        time.setTimeScale(timeScale);
        this.timeScale = timeScale;
    }

}
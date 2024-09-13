public class Particle {

    public Vector3 position;
    public Vector3 velocity;
    public Vector3 acceleration;
    public Vector3 forceAccum;
    public Vector3 gravity = new Vector3(0.0, -15.0, 0.0);

    // inverse mass is 1/mass
    public double inverseMass;
    public double damping;

    public boolean hasFiniteMass = true;

    public void setInfiniteMass() {
        this.hasFiniteMass = false;
    }

    public void setMass(double mass) {
        this.inverseMass = 1/mass;
    }

    public void setInverseMass(double inverseMass) {
        this.inverseMass = inverseMass;
    }

    public double getMass() {
        return 1/this.inverseMass;
    }

    public void clearAccumulator() {
        this.forceAccum = new Vector3(0.0, 0.0, 0.0);
    }

    public void addForce(Vector3 force) {
        this.forceAccum.add(force);
    }

    public void integrate(double duration) {

        if (duration > 0) {
            this.position.addScaled(this.velocity, duration);

            Vector3 newAcceleration = this.acceleration;
            newAcceleration.add(this.gravity);
            newAcceleration.addScaled(this.forceAccum, this.inverseMass);

            this.velocity.addScaled(newAcceleration, duration);
            this.velocity.multiply(Math.pow(this.damping, duration));

            this.clearAccumulator();
        }
    }
}
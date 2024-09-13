import java.lang.Math;
import java.util.ArrayList;

class Vector3 {
    public double x = 0d;
    public double y = 0d;
    public double z = 0d;


    // purely for performance, many machines store 4 float faster than 3
    private double pad;

    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void invert() {
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
    }

    public double magnitude() {
        return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2) + Math.pow(this.z, 2));
    }

    public double square_magnitude() {
        return Math.pow(this.x, 2) + Math.pow(this.y, 2) + Math.pow(this.z, 2);
    }

    public void normalize() {
        double m = this.magnitude();

        if (m > 0) {
            this.x /= m;
            this.y /= m;
            this.z /= m;
        }
    }

    public void multiply(double scalar) {
        this.x *= scalar;
        this.y *= scalar;
        this.z *= scalar;
    }

    public Vector3 newMultiply(double scalar) {
        return new Vector3(this.x * scalar, this.y * scalar, this.z * scalar);
    }

    public void add(Vector3 other) {
        this.x += other.x;
        this.y += other.y;
        this.z += other.z;
    }

    public Vector3 newAdd(Vector3 other) {
        return new Vector3(this.x + other.x, this.y + other.y, this.z + other.z);
    }

    public void subtract(Vector3 other) {
        this.x -= other.x;
        this.y -= other.y;
        this.z -= other.z;
    }

    public Vector3 newSubtract(Vector3 other) {
        return new Vector3(this.x - other.x, this.y - other.y, this.z - other.z);
    }

    public void addScaled(Vector3 other, double scalar) {
        this.x += other.x * scalar;
        this.y += other.y * scalar;
        this.z += other.z * scalar;
    }

    public void componentProduct(Vector3 other) {
        this.x *= other.x;
        this.y *= other.y;
        this.z *= other.z;
    }

    public Vector3 newComponentProduct(Vector3 other) {
        return new Vector3(this.x * other.x, this.y * other.y, this.z * other.z);
    }

    public double scalarProduct(double scalar) {
        return this.x * scalar + this.y * scalar + this.z * scalar;
    }

    public void vectorProduct(Vector3 other) {
        this.x = this.y * other.z - this.z * other.y;
        this.y = this.z * other.x - this.x * other.z;
        this.z = this.x * other.y - this.y * other.x;
    }

    public Vector3 newVectorProduct(Vector3 other) {
        return new Vector3(this.y * other.z - this.z * other.y,this.z * other.x - this.x * other.z, this.x * other.y - this.y * other.x);
    }
}

class Particle {

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
            newAcceleration.addScaled(this.forceAccum, this.inverseMass);

            this.velocity.addScaled(newAcceleration, duration);
            this.velocity.multiply(Math.pow(this.damping, duration));

            this.clearAccumulator();
        }
    }
}

class ParticleForceGenerator {
    public void updateForce(Particle particle, double duration) {}
}

class ParticleForceRegistration {
    Particle particle;
    ParticleForceGenerator fg;

    public ParticleForceRegistration(Particle particle, ParticleForceGenerator fg) {
        this.particle = particle;
        this.fg = fg;
    }
}

class ParticleForceRegistry {
    private ArrayList<ParticleForceRegistration> registrations;

    public void add(Particle particle, ParticleForceGenerator fg) {
        this.registrations.add(new ParticleForceRegistration(particle,fg));
    }
    public void remove(Particle particle, ParticleForceGenerator fg) {}
    public void clear() {}

    public void updateForces(double duration) {

        for (ParticleForceRegistration r : this.registrations) {
            r.fg.updateForce(r.particle, duration);
        }
    }
}

// force generators

class ParticleGravity extends ParticleForceGenerator {
    public Vector3 gravity;

    public ParticleGravity(Vector3 gravity) {
        this.gravity = gravity;
    }

    public void updateForce(Particle particle, double duration) {
        if (!particle.hasFiniteMass) {return;}

        particle.addForce(this.gravity.newMultiply(particle.getMass()));
    }
}

class ParticleDrag extends ParticleForceGenerator {
    public double k1;
    public double k2;

    public ParticleDrag(double k1, double k2) {
        this.k1 = k1;
        this.k2 = k2;
    }

    public void updateForce(Particle particle, double duration) {
        if (!particle.hasFiniteMass) {return;}

        Vector3 force = particle.velocity;

        double dragCoef = force.magnitude();
        dragCoef = k1 * dragCoef + k2 * dragCoef;

        force.normalize();

        force.multiply(-dragCoef);

        particle.addForce(force);

    }
}
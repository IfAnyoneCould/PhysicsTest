import java.util.ArrayList;

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

    public void remove(Particle particle, ParticleForceGenerator fg) {
        this.registrations.remove(new ParticleForceRegistration(particle,fg));
    }

    public void clear() {
        this.registrations.clear();
    }

    public void updateForces(double duration) {

        for (ParticleForceRegistration r : this.registrations) {
            r.fg.updateForce(r.particle, duration);
        }
    }
}

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

        double dragCoeff = force.magnitude();
        dragCoeff = k1 * dragCoeff + k2 * dragCoeff;

        force.normalize();

        force.multiply(-dragCoeff);

        particle.addForce(force);

    }
}
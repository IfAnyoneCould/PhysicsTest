import java.util.ArrayList;
import java.lang.Math;

// set up code
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



//force generators
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

class ParticleSpring extends ParticleForceGenerator {
    Particle other;

    double springConstant;
    double restLength;

    public ParticleSpring(Particle particle, double springConstant, double restLength) {
        this.other = particle;
        this.springConstant = springConstant;
        this.restLength = restLength;
    }

    public void updateForce(Particle particle, double duration) {

        Vector3 force = particle.position;
        force.subtract(other.position);

        double magnitude = force.magnitude();
        magnitude = Math.abs(magnitude - this.restLength);
        magnitude *= springConstant;

        force.normalize();
        force.multiply(-magnitude);
        particle.addForce(force);
    }

    public void updateForceBoth(Particle particle, double duration) {
        this.updateForce(particle, duration);

        Particle temp = this.other;
        this.other = particle;

        this.updateForce(temp, duration);

        this.other = temp;
    }
}

class ParticleAnchoredSpring extends ParticleForceGenerator {

        Vector3 anchor;
        double springConstant;
        double restLength;

        public ParticleAnchoredSpring(Vector3 anchor, double springConstant, double restLength) {
            this.anchor = anchor;
            this.springConstant = springConstant;
            this.restLength = restLength;
        }
        public void updateForce(Particle particle, double duration) {

            Vector3 force = particle.position;
            force.subtract(anchor);

            double magnitude = force.magnitude();
            magnitude = Math.abs(magnitude - this.restLength);
            magnitude *= springConstant;

            force.normalize();
            force.multiply(-magnitude);
            particle.addForce(force);
        }

        void setAnchor(Vector3 anchor) {
            this.anchor = anchor;
        }
}

class ParticleBungee extends ParticleForceGenerator {

    Particle other;

    double springConstant;
    //holds bungee at point when it begins to generate a force
    double restLength;

    public ParticleBungee(Particle particle, double springConstant, double restLength) {
        this.other = particle;
        this.springConstant = springConstant;
        this.restLength = restLength;
    }

    public void updateForce(Particle particle, double duration) {

        Vector3 force = particle.position;
        force.subtract(other.position);

        double magnitude = force.magnitude();
        if (magnitude <= this.restLength) {return;}

        magnitude = springConstant * (this.restLength - magnitude);

        force.normalize();
        force.multiply(-magnitude);
        particle.addForce(force);
    }

    public void updateForceBoth(Particle particle, double duration) {
        this.updateForce(particle, duration);

        Particle temp = this.other;
        this.other = particle;

        this.updateForce(temp, duration);

        this.other = temp;
    }

}

class ParticleAnchoredBungee extends ParticleForceGenerator {
    Vector3 anchor;

    double springConstant;
    //holds bungee at point when it begins to generate a force
    double restLength;

    public ParticleAnchoredBungee(Vector3 anchor, double springConstant, double restLength) {
        this.anchor = anchor;
        this.springConstant = springConstant;
        this.restLength = restLength;
    }

    public void updateForce(Particle particle, double duration) {

        Vector3 force = particle.position;
        force.subtract(anchor);

        double magnitude = force.magnitude();
        if (magnitude <= this.restLength) {return;}

        magnitude = springConstant * (this.restLength - magnitude);

        force.normalize();
        force.multiply(-magnitude);
        particle.addForce(force);
    }

    public void setAnchor(Vector3 anchor) {
        this.anchor = anchor;
    }
}

class ParticleBuoyancy extends ParticleForceGenerator {

    // max depth at which buoyancy force continues to grow
    double maxDepth;
    double volume;
    // height of water above y=0
    double waterHeight;
    double liquidDensity; //water has density of 1000kg per cubit meter

    public ParticleBuoyancy(double maxDepth, double volume, double waterHeight, double liquidDensity) {
        this.maxDepth = maxDepth;
        this.volume = volume;
        this.waterHeight = waterHeight;
        this.liquidDensity = liquidDensity;
    }

    public void updateForce(Particle particle, double duration) {

        double depth = particle.position.y;

        if (depth > waterHeight) {return;}

        Vector3 force = new Vector3(0,0,0);

        if (depth <= waterHeight - maxDepth) {
            force.y = liquidDensity * volume;
            particle.addForce(force);
            return;
        }

        force.y = liquidDensity * volume * (depth - maxDepth - waterHeight);
        particle.addForce(force);
    }


}












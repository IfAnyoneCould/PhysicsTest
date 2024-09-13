
import java.lang.Math;

public class Vector3 {
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
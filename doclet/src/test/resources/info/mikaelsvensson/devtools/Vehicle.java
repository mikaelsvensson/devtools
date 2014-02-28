package info.mikaelsvensson.devtools;

public class Vehicle {
}

interface Motorized {
}

class Car extends Vehicle {
}

class Truck extends Vehicle implements Motorized, HasItems<Trailer> {
}

class Trailer extends Pullable {
}

class TrainCar extends Pullable {
}

interface HasItems<Pullable> {
}

class Pullable extends Vehicle {
}

class Bicycle extends Vehicle implements Motorized {
}

class Train extends Vehicle implements Motorized, HasItems<TrainCar> {
}

class Metro extends Train {
}
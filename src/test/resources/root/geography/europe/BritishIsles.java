package root.geography.europe;

import root.geography.Island;
import root.geography.Nation;
import root.geography.Region;
import root.geography.Territory;

import java.util.Arrays;
import java.util.List;

public class BritishIsles extends Region {
    public List<Island> getIslands() {
        return Arrays.asList(new GreatBritain(), new Ireland(), new IsleOfMan(), new Guernsey(), new Jersey());
    }
    public List<Nation> getNations() {
        return Arrays.asList(new UnitedKingdom(), new RepublicOfIreland());
    }
}

class UnitedKingdom extends Nation {
    public Country[] getCountries() {
        return new Country[]{new NorthernIreland(), new England(), new Wales(), new Scotland()};
    }
}

class Country extends Territory {
}

class GreatBritain implements Island {
}

class BritishIslands extends Region {
}

class Scotland extends Country {
}

class England extends Country {
}

class Wales extends Country {

}

class NorthernIreland extends Country {
}

class Ireland implements Island {
}

class RepublicOfIreland extends Nation {
}

class IsleOfMan implements Island {
}

class Guernsey implements Island {
}

class Jersey implements Island {
}

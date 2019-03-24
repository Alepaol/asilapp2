package asilapp.sms.com.measure.io;

import java.util.UUID;

public  class UUIDs {
    //UUIDS for pressure measurements
    public final static UUID BLOOD_PRESSURE_SERVICES_UUID = convertFromInteger(0x1810) ;
    public final static UUID BLOOD_PRESSURE_CHARACTERISTIC_MEASUREMENT_UUID = convertFromInteger(0x2A35) ;
    public static final UUID CLIENT_CHARACTERISTIC_CONFIG = convertFromInteger(0x2902);
    //public final static UUID BLOOD_PRESSURE_CHARACTERISTIC_INTERMEDIATE_CUFF_PRESSURE_UUID = convertFromInteger(0x2A36) ;
    //public final static UUID BLOOD_PRESSURE_CHARACTERISTIC_FEATURE_UUID = convertFromInteger(0x2A49) ;

    private static UUID convertFromInteger(int i) {
        final long MSB = 0x0000000000001000L;
        final long LSB = 0x800000805f9b34fbL;
        long value = i & 0xFFFFFFFF;
        return new UUID(MSB | (value << 32), LSB);
    }
}

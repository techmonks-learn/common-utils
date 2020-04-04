package com.tm.common.encryption;

public class Enums {
    public enum EncryptionAlgorithm {
        AES(1),
        AES_GCM(2),
        AES_ECB(3),
        RSA(4);

        private Integer algorithm;

        EncryptionAlgorithm(Integer i) {
            this.algorithm = i;
        }

        public Integer value() {
            return this.algorithm;
        }
    }

    public enum HashingAlgorithm {

        SHA256(3),
        SHA512(4);

        private Integer algorithm;

        HashingAlgorithm(Integer i) {
            this.algorithm = i;
        }

        public Integer value() {
            return this.algorithm;
        }
    }


}

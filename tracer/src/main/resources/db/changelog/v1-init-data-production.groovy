databaseChangeLog() {

    changeSet(id: '0', author: 'araichev') {
        insert(tableName: 'tests') {
            column(name: 'name', value: 'FREQUENCY')
            column(name: 'type', value: 'NIST')
            column(name: 'parametrized', value: 'false')
        }
        insert(tableName: 'tests') {
            column(name: 'name', value: 'BLOCK_FREQUENCY')
            column(name: 'type', value: 'NIST')
            column(name: 'parametrized', value: 'true')
        }
        insert(tableName: 'tests') {
            column(name: 'name', value: 'BINARY_MATRIX')
            column(name: 'type', value: 'NIST')
            column(name: 'parametrized', value: 'true')
        }
        insert(tableName: 'tests') {
            column(name: 'name', value: 'NON_OVERLAPPING_TEMPLATE')
            column(name: 'type', value: 'NIST')
            column(name: 'parametrized', value: 'true')
        }
        insert(tableName: 'tests') {
            column(name: 'name', value: 'OVERLAPPING_TEMPLATE')
            column(name: 'type', value: 'NIST')
            column(name: 'parametrized', value: 'true')
        }
        insert(tableName: 'tests') {
            column(name: 'name', value: 'SPECTRAL')
            column(name: 'type', value: 'NIST')
            column(name: 'parametrized', value: 'false')
        }
        insert(tableName: 'tests') {
            column(name: 'name', value: 'LINEAR_COMPLEXITY')
            column(name: 'type', value: 'NIST')
            column(name: 'parametrized', value: 'true')
        }
        insert(tableName: 'tests') {
            column(name: 'name', value: 'LONGEST_RUN_OF_ONES')
            column(name: 'type', value: 'NIST')
            column(name: 'parametrized', value: 'false')
        }
        insert(tableName: 'tests') {
            column(name: 'name', value: 'MAURERS')
            column(name: 'type', value: 'NIST')
            column(name: 'parametrized', value: 'true')
        }
        insert(tableName: 'tests') {
            column(name: 'name', value: 'CUMULATIVE_SUMS')
            column(name: 'type', value: 'NIST')
            column(name: 'parametrized', value: 'false')
        }
        insert(tableName: 'tests') {
            column(name: 'name', value: 'RANDOM_EXCURSIONS')
            column(name: 'type', value: 'NIST')
            column(name: 'parametrized', value: 'false')
        }

        insert(tableName: 'tests') {
            column(name: 'name', value: 'EXCURSION_VARIANT')
            column(name: 'type', value: 'NIST')
            column(name: 'parametrized', value: 'false')
        }

        insert(tableName: 'tests') {
            column(name: 'name', value: 'RUNS')
            column(name: 'type', value: 'NIST')
            column(name: 'parametrized', value: 'false')
        }

        insert(tableName: 'tests') {
            column(name: 'name', value: 'SERIAL')
            column(name: 'type', value: 'NIST')
            column(name: 'parametrized', value: 'true')
        }

        insert(tableName: 'tests') {
            column(name: 'name', value: 'ENTROPY')
            column(name: 'type', value: 'NIST')
            column(name: 'parametrized', value: 'true')
        }

        insert(tableName: 'tests') {
            column(name: 'name', value: 'ONE')
            column(name: 'type', value: 'MD')
            column(name: 'parametrized', value: 'false')
        }
        insert(tableName: 'tests') {
            column(name: 'name', value: 'TWO')
            column(name: 'type', value: 'MD')
            column(name: 'parametrized', value: 'false')
        }
        insert(tableName: 'tests') {
            column(name: 'name', value: 'THREE')
            column(name: 'type', value: 'MD')
            column(name: 'parametrized', value: 'false')
        }
        insert(tableName: 'tests') {
            column(name: 'name', value: 'FOUR')
            column(name: 'type', value: 'MD')
            column(name: 'parametrized', value: 'false')
        }
        insert(tableName: 'tests') {
            column(name: 'name', value: 'FIVE')
            column(name: 'type', value: 'MD')
            column(name: 'parametrized', value: 'false')
        }
        insert(tableName: 'tests') {
            column(name: 'name', value: 'SIX')
            column(name: 'type', value: 'MD')
            column(name: 'parametrized', value: 'false')
        }
        insert(tableName: 'tests') {
            column(name: 'name', value: 'SEVEN')
            column(name: 'type', value: 'MD')
            column(name: 'parametrized', value: 'false')
        }

        insert(tableName: 'tests') {
            column(name: 'name', value: 'EIGHT')
            column(name: 'type', value: 'MD')
            column(name: 'parametrized', value: 'false')
        }

        insert(tableName: 'applications') {
            column(name: 'name', value: 'sender-service')
        }

        insert(tableName: 'applications') {
            column(name: 'name', value: 'email-service')
        }

        insert(tableName: 'applications') {
            column(name: 'name', value: 'lt-service')
        }

        insert(tableName: 'labels') {
            column(name: 'label', value: 'java')
        }

        insert(tableName: 'labels') {
            column(name: 'label', value: 'decimal')
        }

        insert(tableName: 'labels') {
            column(name: 'label', value: 'hardware')
        }

        insert(tableName: 'labels') {
            column(name: 'label', value: 'binary')
        }

        insert(tableName: 'label_test') {
            column(name: 'label_id', value: 1)
            column(name: 'test_id', value: 1)
        }

        insert(tableName: 'label_test') {
            column(name: 'label_id', value: 1)
            column(name: 'test_id', value: 6)
        }

        insert(tableName: 'label_test') {
            column(name: 'label_id', value: 1)
            column(name: 'test_id', value: 8)
        }

        insert(tableName: 'label_test') {
            column(name: 'label_id', value: 1)
            column(name: 'test_id', value: 10)
        }

        insert(tableName: 'label_test') {
            column(name: 'label_id', value: 1)
            column(name: 'test_id', value: 11)
        }

        insert(tableName: 'label_test') {
            column(name: 'label_id', value: 1)
            column(name: 'test_id', value: 12)
        }

        insert(tableName: 'label_test') {
            column(name: 'label_id', value: 1)
            column(name: 'test_id', value: 13)
        }

        insert(tableName: 'label_test') {
            column(name: 'label_id', value: 4)
            column(name: 'test_id', value: 1)
        }

        insert(tableName: 'label_test') {
            column(name: 'label_id', value: 4)
            column(name: 'test_id', value: 6)
        }

        insert(tableName: 'label_test') {
            column(name: 'label_id', value: 4)
            column(name: 'test_id', value: 8)
        }

        insert(tableName: 'label_test') {
            column(name: 'label_id', value: 4)
            column(name: 'test_id', value: 17)
        }

        insert(tableName: 'label_test') {
            column(name: 'label_id', value: 4)
            column(name: 'test_id', value: 19)
        }

        insert(tableName: 'label_test') {
            column(name: 'label_id', value: 4)
            column(name: 'test_id', value: 22)
        }

        insert(tableName: 'label_test') {
            column(name: 'label_id', value: 3)
            column(name: 'test_id', value: 16)
        }

        insert(tableName: 'label_test') {
            column(name: 'label_id', value: 3)
            column(name: 'test_id', value: 17)
        }

        insert(tableName: 'label_test') {
            column(name: 'label_id', value: 3)
            column(name: 'test_id', value: 18)
        }

        insert(tableName: 'label_test') {
            column(name: 'label_id', value: 3)
            column(name: 'test_id', value: 19)
        }

        insert(tableName: 'label_test') {
            column(name: 'label_id', value: 3)
            column(name: 'test_id', value: 20)
        }

        insert(tableName: 'label_test') {
            column(name: 'label_id', value: 3)
            column(name: 'test_id', value: 21)
        }

        insert(tableName: 'label_test') {
            column(name: 'label_id', value: 3)
            column(name: 'test_id', value: 22)
        }

        insert(tableName: 'label_test') {
            column(name: 'label_id', value: 3)
            column(name: 'test_id', value: 23)
        }

        insert(tableName: 'alerts') {
            column(name: 'name', value: 'java-alert')
        }

        insert(tableName: 'alerts') {
            column(name: 'name', value: 'email-alert')
        }

        insert(tableName: 'alerts') {
            column(name: 'name', value: 'test-alert')
        }

        insert(tableName: 'label_alert') {
            column(name: 'label_id', value: 1)
            column(name: 'alert_id', value: 1)
        }

        insert(tableName: 'label_alert') {
            column(name: 'label_id', value: 2)
            column(name: 'alert_id', value: 1)
        }

        insert(tableName: 'label_alert') {
            column(name: 'label_id', value: 3)
            column(name: 'alert_id', value: 2)
        }

        insert(tableName: 'label_alert') {
            column(name: 'label_id', value: 4)
            column(name: 'alert_id', value: 2)
        }

        insert(tableName: 'contacts') {
            column(name: 'email', value: 'maxhsf@gmail.com')
            column(name: 'alert_id', value: 1)
        }

        insert(tableName: 'contacts') {
            column(name: 'email', value: 'localhost@gmail.com')
            column(name: 'alert_id', value: 1)
        }

        insert(tableName: 'contacts') {
            column(name: 'email', value: 'root@gmail.com')
            column(name: 'alert_id', value: 1)
        }

        insert(tableName: 'contacts') {
            column(name: 'email', value: 'admin@ukr.net')
            column(name: 'alert_id', value: 2)
        }

        insert(tableName: 'contacts') {
            column(name: 'email', value: 'root@ukr.net')
            column(name: 'alert_id', value: 2)
        }
    }
}
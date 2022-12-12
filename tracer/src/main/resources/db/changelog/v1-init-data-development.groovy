databaseChangeLog() {

    changeSet(id: '0', author: 'araichev') {
        insert(tableName: 'applications') {
            column(name: 'name', value: 'lean-app')
        }

        insert(tableName: 'applications') {
            column(name: 'name', value: 'sender-app')
        }

        insert(tableName: 'applications') {
            column(name: 'name', value: 'email-app')
        }

        insert(tableName: 'applications') {
            column(name: 'name', value: 'lt-service')
        }

        insert(tableName: 'traces') {
            column(name: 'value', value: '140473890')
            column(name: 'application_id', value: 1)
            column(name: 'generated_at', value: '2022-09-05 16:03:48.000000')
        }

        insert(tableName: 'traces') {
            column(name: 'value', value: '970859842')
            column(name: 'application_id', value: 1)
            column(name: 'generated_at', value: '2022-09-05 16:04:14.000000')
        }

        insert(tableName: 'traces') {
            column(name: 'value', value: '111001110111100010010101000010')
            column(name: 'application_id', value: 2)
            column(name: 'generated_at', value: '2022-09-05 16:05:22.000000')
        }

        insert(tableName: 'traces') {
            column(name: 'value', value: '11101101001000100111100101010010000111111111010101011110010011011000001110000100110101')
            column(name: 'application_id', value: 2)
            column(name: 'generated_at', value: '2022-09-05 16:05:27.000000')
        }

        insert(tableName: 'traces') {
            column(name: 'value', value: '792361604')
            column(name: 'application_id', value: 1)
            column(name: 'generated_at', value: '2022-09-09 16:03:48.000000')
        }

        insert(tableName: 'traces') {
            column(name: 'value', value: '17252179')
            column(name: 'application_id', value: 1)
            column(name: 'generated_at', value: '2022-09-04 16:04:14.000000')
        }

        insert(tableName: 'traces') {
            column(name: 'value', value: '169375527')
            column(name: 'application_id', value: 2)
            column(name: 'generated_at', value: '2022-09-03 16:00:22.000000')
        }

        insert(tableName: 'traces') {
            column(name: 'value', value: '872301691')
            column(name: 'application_id', value: 2)
            column(name: 'generated_at', value: '2022-09-02 12:00:27.000000')
        }

        insert(tableName: 'traces') {
            column(name: 'value', value: '169375742541229527')
            column(name: 'application_id', value: 3)
            column(name: 'generated_at', value: '2022-08-10 10:00:22.000000')
        }

        insert(tableName: 'traces') {
            column(name: 'value', value: '872756337122301697563371221')
            column(name: 'application_id', value: 3)
            column(name: 'generated_at', value: '2022-08-25 17:39:07.000000')
        }

        insert(tableName: 'traces') {
            column(name: 'value', value: '169375722301697563342541229527')
            column(name: 'application_id', value: 3)
            column(name: 'generated_at', value: '2022-08-10 10:00:22.000000')
        }

        insert(tableName: 'traces') {
            column(name: 'value', value: '872756337122230169756332301697563371221')
            column(name: 'application_id', value: 3)
            column(name: 'generated_at', value: '2022-08-25 17:39:07.000000')
        }


        insert(tableName: 'traces') {
            column(name: 'value', value: '169375742223016975633541229527')
            column(name: 'application_id', value: 3)
            column(name: 'generated_at', value: '2022-08-10 10:00:22.000000')
        }

        insert(tableName: 'traces') {
            column(name: 'value', value: '872756337122230169756332301697563371221')
            column(name: 'application_id', value: 3)
            column(name: 'generated_at', value: '2022-08-25 17:39:07.000000')
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

        insert(tableName: 'label_trace') {
            column(name: 'label_id', value: 1)
            column(name: 'trace_id', value: 1)
        }

        insert(tableName: 'label_trace') {
            column(name: 'label_id', value: 4)
            column(name: 'trace_id', value: 1)
        }

        insert(tableName: 'label_trace') {
            column(name: 'label_id', value: 1)
            column(name: 'trace_id', value: 2)
        }

        insert(tableName: 'label_trace') {
            column(name: 'label_id', value: 4)
            column(name: 'trace_id', value: 2)
        }

        insert(tableName: 'label_trace') {
            column(name: 'label_id', value: 1)
            column(name: 'trace_id', value: 4)
        }

        insert(tableName: 'label_trace') {
            column(name: 'label_id', value: 1)
            column(name: 'trace_id', value: 3)
        }

        insert(tableName: 'label_trace') {
            column(name: 'label_id', value: 3)
            column(name: 'trace_id', value: 3)
        }

        insert(tableName: 'label_trace') {
            column(name: 'label_id', value: 4)
            column(name: 'trace_id', value: 3)
        }

        insert(tableName: 'label_trace') {
            column(name: 'label_id', value: 3)
            column(name: 'trace_id', value: 4)
        }

        insert(tableName: 'label_trace') {
            column(name: 'label_id', value: 4)
            column(name: 'trace_id', value: 4)
        }

        insert(tableName: 'label_trace') {
            column(name: 'label_id', value: 1)
            column(name: 'trace_id', value: 5)
        }

        insert(tableName: 'label_trace') {
            column(name: 'label_id', value: 1)
            column(name: 'trace_id', value: 6)
        }

        insert(tableName: 'label_trace') {
            column(name: 'label_id', value: 1)
            column(name: 'trace_id', value: 7)
        }

        insert(tableName: 'label_trace') {
            column(name: 'label_id', value: 1)
            column(name: 'trace_id', value: 8)
        }

        insert(tableName: 'label_trace') {
            column(name: 'label_id', value: 1)
            column(name: 'trace_id', value: 9)
        }

        insert(tableName: 'label_trace') {
            column(name: 'label_id', value: 1)
            column(name: 'trace_id', value: 10)
        }

        insert(tableName: 'label_trace') {
            column(name: 'label_id', value: 4)
            column(name: 'trace_id', value: 8)
        }

        insert(tableName: 'label_trace') {
            column(name: 'label_id', value: 4)
            column(name: 'trace_id', value: 9)
        }

        insert(tableName: 'test_runs') {
            column(name: 'trace_id', value: 1)
            column(name: 'test_id', value: 1)
            column(name: 'test_result', value: 0.1)
            column(name: 'random', value: true)
        }

        insert(tableName: 'test_runs') {
            column(name: 'trace_id', value: 1)
            column(name: 'test_id', value: 2)
            column(name: 'test_result', value: 0.3910)
            column(name: 'random', value: false)
        }

        insert(tableName: 'test_runs') {
            column(name: 'trace_id', value: 1)
            column(name: 'test_id', value: 16)
            column(name: 'test_result', value: 0.99999)
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
            column(name: 'alert_id', value: 2)
        }

        insert(tableName: 'label_alert') {
            column(name: 'label_id', value: 3)
            column(name: 'alert_id', value: 2)
        }

        insert(tableName: 'label_alert') {
            column(name: 'label_id', value: 1)
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

        insert(tableName: 'alert_events') {
            column(name: 'alert_id', value: 1)
            column(name: 'status', value: 'RESOLVED')
            column(name: 'reason', value: 'FREQUENCY and SPECTRAL tests failed')
            column(name: 'trace_id', value: 1)
        }

        insert(tableName: 'alert_events') {
            column(name: 'alert_id', value: 1)
            column(name: 'status', value: 'NEW')
            column(name: 'reason', value: 'Frequency of random numbers within region exceeds expected value')
            column(name: 'trace_id', value: 2)
        }
    }
}
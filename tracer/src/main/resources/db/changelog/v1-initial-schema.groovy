package db.changelog

databaseChangeLog() {

    changeSet(id: '1', author: 'araichev') {
        createTable(tableName: 'labels') {
            column(name: 'id', type: 'serial') {
                constraints(nullable: false, primaryKey: true, primaryKeyName: "label_id")
            }
            column(name: 'label', type: 'varchar(50)') {
                constraints(nullable: false, unique: true)
            }
        }

        sql """
        CREATE TYPE tracer.test AS ENUM (
        'FREQUENCY',
        'BLOCK_FREQUENCY', 
        'BINARY_MATRIX', 
        'NON_OVERLAPPING_TEMPLATE', 
        'OVERLAPPING_TEMPLATE', 
        'SPECTRAL',
        'LINEAR_COMPLEXITY',
        'LONGEST_RUN_OF_ONES',
        'MAURERS',
        'CUMULATIVE_SUMS',
        'RANDOM_EXCURSIONS',
        'EXCURSION_VARIANT',
        'RUNS',
        'SERIAL',
        'ENTROPY',
        'ONE',
        'TWO',
        'THREE',
        'FOUR',
        'FIVE',
        'SIX',
        'SEVEN',
        'EIGHT'
        )
        """

        sql """
        CREATE TYPE tracer.test_type AS ENUM (
        'NIST',
        'MD'
        )
        """

        createTable(tableName: 'tests') {
            column(name: 'id', type: 'serial') {
                constraints(nullable: false, primaryKey: true, primaryKeyName: "test_id")
            }
            column(name: 'name', type: 'tracer.test') {
                constraints(nullable: false, unique: true)
            }
            column(name: 'type', type: 'tracer.test_type') {
                constraints(nullable: false)
            }

            column(name: 'parametrized', type: 'boolean') {
                constraints(nullable: false)
            }
        }

        addUniqueConstraint(
                tableName: 'tests',
                columnNames: 'name, type',
                constraintName: 'name_type_unique'
        )


        createTable(tableName: 'label_test') {
            column(name: 'label_id', type: 'int') {
                constraints(nullable: false)
            }
            column(name: 'test_id', type: 'int') {
                constraints(nullable: false)
            }
            column(name: 'parameters', type: 'json') {
                constraints(nullable: true)
            }
        }

        addPrimaryKey(
                columnNames: "label_id, test_id",
                constraintName: "label_test_id",
                tableName: "label_test"
        )

        addForeignKeyConstraint(
                baseTableName: 'label_test',
                baseColumnNames: 'label_id',
                constraintName: 'label_test_labels_fk',
                referencedTableName: 'labels',
                referencedColumnNames: 'id'
        )

        addForeignKeyConstraint(
                baseTableName: 'label_test',
                baseColumnNames: 'test_id',
                constraintName: 'label_test_tests_fk',
                referencedTableName: 'tests',
                referencedColumnNames: 'id'
        )

        createTable(tableName: 'alerts') {
            column(name: 'id', type: 'serial') {
                constraints(nullable: false, primaryKey: true, primaryKeyName: "alert_id")
            }
            column(name: 'name', type: 'varchar(50)') {
                constraints(nullable: false, unique: true)
            }
        }

        createTable(tableName: 'label_alert') {
            column(name: 'label_id', type: 'int') {
                constraints(nullable: false)
            }
            column(name: 'alert_id', type: 'int') {
                constraints(nullable: false)
            }
        }

        addPrimaryKey(
                columnNames: "label_id, alert_id",
                constraintName: "label_alert_id",
                tableName: "label_alert"
        )

        addForeignKeyConstraint(
                baseTableName: 'label_alert',
                baseColumnNames: 'label_id',
                constraintName: 'label_alert_labels_fk',
                referencedTableName: 'labels',
                referencedColumnNames: 'id'
        )

        addForeignKeyConstraint(
                baseTableName: 'label_alert',
                baseColumnNames: 'alert_id',
                constraintName: 'label_alert_alerts_fk',
                referencedTableName: 'alerts',
                referencedColumnNames: 'id'
        )

        createTable(tableName: 'contacts') {
            column(name: 'id', type: 'serial') {
                constraints(nullable: false, primaryKey: true, primaryKeyName: "contact_id")
            }
            column(name: 'alert_id', type: 'int') {
                constraints(nullable: false)
            }
            column(name: 'email', type: 'varchar(100)') {
                constraints(nullable: false)
            }
        }

        addUniqueConstraint(
                tableName: 'contacts',
                columnNames: 'alert_id, email',
                constraintName: 'alert_id_email_unique'
        )

        addForeignKeyConstraint(
                baseTableName: 'contacts',
                baseColumnNames: 'alert_id',
                constraintName: 'contacts_alert_fk',
                referencedTableName: 'alerts',
                referencedColumnNames: 'id'
        )

        createTable(tableName: 'traces') {
            column(name: 'id', type: 'bigserial') {
                constraints(nullable: false, primaryKey: true, primaryKeyName: "traces_id")
            }
            column(name: 'value', type: 'varchar(255)') {
                constraints(nullable: false)
            }
            column(name: 'application_id', type: 'int') {
                constraints(nullable: false)
            }
            column(name: 'generated_at', type: 'timestamp') {
                constraints(nullable: false)
            }
        }

        createTable(tableName: 'label_trace') {
            column(name: 'label_id', type: 'int') {
                constraints(nullable: false)
            }
            column(name: 'trace_id', type: 'bigint') {
                constraints(nullable: false)
            }
        }

        addPrimaryKey(
                columnNames: "label_id, trace_id",
                constraintName: "label_trace_id",
                tableName: "label_trace"
        )

        addForeignKeyConstraint(
                baseTableName: 'label_trace',
                baseColumnNames: 'label_id',
                constraintName: 'label_trace_labels_fk',
                referencedTableName: 'labels',
                referencedColumnNames: 'id'
        )

        addForeignKeyConstraint(
                baseTableName: 'label_trace',
                baseColumnNames: 'trace_id',
                constraintName: 'label_trace_traces_fk',
                referencedTableName: 'traces',
                referencedColumnNames: 'id'
        )

        createTable(tableName: 'test_runs') {
            column(name: 'id', type: 'bigserial') {
                constraints(nullable: false, primaryKey: true, primaryKeyName: "test_run_id")
            }
            column(name: 'trace_id', type: 'bigint') {
                constraints(nullable: false)
            }
            column(name: 'test_id', type: 'int') {
                constraints(nullable: false)
            }
            column(name: 'test_result', type: 'numeric(11,10)') {
                constraints(nullable: false)
            }
            column(name: 'random', type: 'boolean') {
                constraints(nullable: true)
            }
        }

        addForeignKeyConstraint(
                baseTableName: 'test_runs',
                baseColumnNames: 'trace_id',
                constraintName: 'test_runs_traces_fk',
                referencedTableName: 'traces',
                referencedColumnNames: 'id'
        )

        addForeignKeyConstraint(
                baseTableName: 'test_runs',
                baseColumnNames: 'test_id',
                constraintName: 'test_runs_tests_fk',
                referencedTableName: 'tests',
                referencedColumnNames: 'id'
        )


        createTable(tableName: 'applications') {
            column(name: 'id', type: 'serial') {
                constraints(nullable: false, primaryKey: true, primaryKeyName: "application_id")
            }
            column(name: 'name', type: 'varchar(50)') {
                constraints(nullable: false, unique: true)
            }
        }

        addForeignKeyConstraint(
                baseTableName: 'traces',
                baseColumnNames: 'application_id',
                constraintName: 'traces_application_fk',
                referencedTableName: 'applications',
                referencedColumnNames: 'id'
        )


        createTable(tableName: 'alert_events') {
            column(name: 'id', type: 'bigserial') {
                constraints(nullable: false, primaryKey: true, primaryKeyName: "alert_event_id")
            }
            column(name: 'alert_id', type: 'int') {
                constraints(nullable: false)
            }
            column(name: 'status', type: 'varchar(10)') {
                constraints(nullable: false)
            }
            column(name: 'reason', type: 'varchar') {
                constraints(nullable: false)
            }
            column(name: 'trace_id', type: 'bigint') {
                constraints(nullable: false)
            }
        }

        addForeignKeyConstraint(
                baseTableName: 'alert_events',
                baseColumnNames: 'alert_id',
                constraintName: 'alert_event_alerts_fk',
                referencedTableName: 'alerts',
                referencedColumnNames: 'id'
        )

        addForeignKeyConstraint(
                baseTableName: 'alert_events',
                baseColumnNames: 'trace_id',
                constraintName: 'alert_event_traces_fk',
                referencedTableName: 'traces',
                referencedColumnNames: 'id'
        )

    }
}
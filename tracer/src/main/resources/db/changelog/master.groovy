package db.changelog

databaseChangeLog{
    include(file: "db/changelog/v1-initial-schema.groovy")
    include(file: "db/changelog/v1-init-data-production.groovy")
    include(file: "db/changelog/v1-init-data-development.groovy", context: 'development')

}
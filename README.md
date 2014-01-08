Generality
==========

Generality - Another Template Based Generator.

Generality it's a tool to manage [freemarker](http://freemarker.org/) templates and use them with relational databases.

## How to use

Simply create a `Template Project` and create some files with an `.ftl` extension that contains your freemarker templates and then right click on the template project and click on `Run template`

If you want some examples, Generality comes with some predefined templates so you can see how it looks like. Also there's a **GenericTemplate** that contains a file named **properties.txt.ftl** with an example of all the properties that you can access in a freemaker template using generality.

## What does `Run template` exactly do?

It will ask you for your database connection and then about some information of your database structure in order to fill the template metadata with the database information extracted from the `jdbc` metadata information.

## What its this metadata thing?

The metadata its the information obtained from a jdbc connection when the underlayer database is connected.

## How this metadata it's used?

The metada includes various informations. Like the database name, selected tables, columns, exported keys, primary keys of a table, etc. All of this information its available throught some reserved freemarker variables when using the `Run template` option in Generality.

Here are some of the variables explained:

- **tables (string):** Contains the name of the tables included in the template. The structure of this variable is:
- **tableName (string):** The name of the table as obtained from the database.
- **tableNameCamelCase (string):** the name of the current table parsed in camelCase.
- **columns (sequence):** every column metada of the table.
- **columnName (string):** name of the column.
- **dataType (number):** a integer indicating the data type of the column. It's the same integer used in the [Jdbc Types](http://docs.oracle.com/javase/6/docs/api/java/sql/Types.html) constants. 
- **columnSize (number):** column size of the column.
- **primaryKey (boolean):** true if this column is in a primary key constraint.
- **nullable (boolean):** true if this column is nullable.

## Collaborate

If you want to collaborate and add some more functionality, please don't hesitate to download the source code at [https://github.com/neowinx/generality](https://github.com/neowinx/generality) and push some stuff.

### The Architecture

The project itself it's just a eclipse rich client plattform project with some library jars (freemarker, postgresql, etc.) and the freemarker editor feature of the [Jboss Tools Project](http://freemarker.org/editors.html) as explained [here](http://freemarker.org/editors.html) for the syntax highlighting stuff.

### Requirements

The requirements to develop this project are:

- *Eclipse:* Some eclipse version.
- *Git:* obviously.
- *Jboss Tools:* plugin of the eclipse, beware of the version.
- *Some Ideas:* well this actually should be where you wanna start :)

Cheers!

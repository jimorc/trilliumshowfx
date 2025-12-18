# How to Contribute

There are many ways that you can contribute to the trilliumshowfx project:

1. Submit bug reports for any bugs that you find.
2. Submit feature requests for any new functionality.
3. Update the documentation, both in this project and the Wiki.
4. Submit bug fixes and feature enhancements via pull requests (PRs).

Each of these is covered below.

## Bug Reports

Whenever you come across something that does not work, or documentation that
does not match the program, make sure that you are using the latest version of the program. Next, check that the bug has not already been reported.

If the bug has already been reported, is there any more information that you can
add? If the bug has not been reported, please add an issue via the Issues
tab on the project's home page.

## Feature Requests

Before you request a feature, please ensure that you are using the latest
released version of the program. Now check that the feature has not already been
requested. If the feature has already been requested, is there any additional
information that you can provide?

If the feature has not already been requested, please add an issue via the
Issues tab on the project's home page.

## Documentation

If the Wiki documentation does not match the current latest version of the
program, please:

1. Issue a bug report outlining where the discrepancies are.
2. If you wish to provide updates to the documentation, please do a pull
request (PR) based on the *main* branch of the project or the Wiki.

## Pull Requests

1. For any showstopper bugs, create a pull request based on the *main* branch.
2. For any minor bugs, create a pull request based on the *develop* branch.

For all PRs, you should do the following:

1. For fixing a bug, first generate one or more unit tests that illustrate
the bug.
2. For any code that you generate, create as many unit tests as you can to
exercise the code. As many tests as required should be written to exercise
the paths through your new code. Limit the number of tests to those required
to exercise your code without duplicating the tests.
3. Follow the code style guidelines outlined below.
4. In addition to any code testing, and program running and debugging
that you perform using an IDE, run the following from a command line:
```bash
mvn clean
mvn package
mvn jpackage:jpackage@xxx
```
where "xxx" represents the target for the operating system you are running
on:

* *mac* for MacOS.
* *win* for Windows 11 and later.
* *deb* for Debian-based Linux systems.

5. Repeat the builds on as many operating systems and architectures that you
have access to.

### Code Style Guidelines

A good place to start is with the
[Code Conventions for the Java Programming Language](
    https://checkstyle.sourceforge.io/styleguides/sun-code-conventions-19990420/CodeConvTOC.doc.html).
The style checking specified in the `pom.xml` file matches this guide,
with a few exceptions. For more information, see
[checkstyle-rules](https://github.com/ngeor/checkstyle-rules). I have also
made a couple of modifications to the rules because I found them overly
restrictive.

The style checks in the `pom.xml` file run every time you execute
`mvn package` or `mvn test` or any number of other `mvn` commands.

The commands will error if any style check fails.

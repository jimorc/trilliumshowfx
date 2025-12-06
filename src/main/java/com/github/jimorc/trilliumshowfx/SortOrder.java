package com.github.jimorc.trilliumshowfx;

/**
 * sortOrder defines possible sort orders for InputCSV lines.
*/
public enum SortOrder {
    /**
     * AsIs denotes sorting in the order than full names first appear in CSV file.
     */
    AsIs,
    /**
     * AlphabeticalByFullName denotes sorting in alphabetical order by full name.
     */
    AlphabeticalByFullName,
    /**
     * AlphabeticalByLastNameThenFirstName denotes sorting in alphabetical order by last name then first name.
     */
    AlphabeticalByLastNameThenFirstName,
    /**
     * AlphabeticalByFullNameReverse denotes sorting in reverse alphabetical order by full name.
     */
    AlphabeticalByFullNameReverse,
    /**
     * AlphabeticalByLastNameThenFirstNameReverse denotes sorting in reverse alphabetical order by last
     * name then first name.
     */
    AlphabeticalByLastNameThenFirstNameReverse
}

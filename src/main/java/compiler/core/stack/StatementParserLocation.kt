package compiler.core.stack

enum class StatementParserLocation {
    LOCATION_START,
    LOCATION_DO,
    LOCATION_FOR,
    LOCATION_IF,
    LOCATION_ELSE,
    LOCATION_WHILE,
    LOCATION_BASIC_BLOCK
}
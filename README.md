# Comparative Study

This comparative study is based on a simple Android application for user and their contacts registration.

The comparative strategies are:

- Field validations using [Android Saripaar][1] library
- Field validations using [Convalida][2] library
- Field validations with codes written manually with and with code reuse
- Field validations with codes written manually with and without code reuse

## Lines of code using the libraries

|         #        | User Login | User Registration | Contact Registration |
|:----------------:|:----------:|:-----------------:|:--------------------:|
| Android Saripaar |     20     |         23        |          21          |
|     Convalida    |      5     |         7         |           7          |

## Lines of code with codes written manually

|          #         | User Login | User Registration | Contact Registration | Reused Code | Total |
|:------------------:|:----------:|:-----------------:|:--------------------:|:-----------:|:-----:|
| Without Code Reuse |     29     |         61        |          45          |      0      |  135  |
|   With Code Reuse  |      5     |         7         |           6          |      68     |   86  |

## Total of lines of code

|          #         | Total |
|:------------------:|:-----:|
|  Android Saripaar  |   64  |
|      Convalida     |   19  |
| Without Code Reuse |  135  |
|   With Code Reuse  |   86  |

[1]: https://github.com/ragunathjawahar/android-saripaar
[2]: https://github.com/WellingtonCosta/convalida
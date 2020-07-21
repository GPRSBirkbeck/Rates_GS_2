# Rates GS

## Architecture
### VMMV
This implementation will use a View model Viewmodel architecture, as this is Android best practice (see android architecture components)
### Classes
There are X classes - SOLVE FOR X

## Room
Database used here is Room, as this is best practice, and what is used at Revolut

## Dagger2

## RxJava2
RxJAva2 is used to manage the monetary values displayed for non-first receiver rates.
These rates observe an observable that combines the live rate from the API with the input from the first receiver.
The first receiver uses a library from Jake Wharton to make the edittext an oberservable.

## Localised content

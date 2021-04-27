package com.ardorapps.demovl

class EndOfPeopleListException : Exception() {

    override fun getLocalizedMessage(): String? {
        return "End of the list!"
    }
}
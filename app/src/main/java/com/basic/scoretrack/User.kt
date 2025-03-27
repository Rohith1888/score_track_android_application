package com.basic.scoretrack


class User{
    val fullName: String
    val email: String
    val password: String
    constructor(fullName: String, email: String, password: String){
        this.email = email
        this.fullName = fullName
        this.password = password
    }
}

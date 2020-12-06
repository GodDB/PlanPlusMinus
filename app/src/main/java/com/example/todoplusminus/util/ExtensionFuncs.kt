package com.example.todoplusminus.util

import java.time.LocalDate

fun LocalDate.copy() =
    LocalDate.of(this.year, this.month, this.dayOfMonth)
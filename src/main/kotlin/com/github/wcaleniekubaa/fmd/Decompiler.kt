package com.github.wcaleniekubaa.fmd

import java.io.File

enum class Decompiler(val executor: (File) -> Unit) {}

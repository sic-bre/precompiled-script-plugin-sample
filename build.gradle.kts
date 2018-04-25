plugins {
    `base`
    // Plugins way to apply build script binary plugins
    id("plugins.precompiled")
    id("plugins.standard")
}

// Alternate way to apply based on class name alone
apply<plugins.PrecompiledPlugin>()
apply<plugins.StandardPlugin>()

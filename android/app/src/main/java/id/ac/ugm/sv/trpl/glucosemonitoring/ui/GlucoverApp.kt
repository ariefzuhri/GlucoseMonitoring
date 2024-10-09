package id.ac.ugm.sv.trpl.glucosemonitoring.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import id.ac.ugm.sv.trpl.glucosemonitoring.ui.navigation.NavGraph

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GlucoverApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    Scaffold(
        modifier = modifier,
    ) { innerPadding ->
        NavGraph(
            navController = navController,
            modifier = Modifier.padding(innerPadding),
        )
    }
}
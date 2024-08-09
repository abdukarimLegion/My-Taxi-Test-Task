package uz.mango.my_taxi_test_task.presentation.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import uz.mango.my_taxi_test_task.R


private val Lato = FontFamily(
    Font(R.font.lato_regular),
    Font(R.font.lato_medium, FontWeight.W500),
    Font(R.font.lato_semibold, FontWeight.W600),
    Font(R.font.lato_bold, FontWeight.W700)
)

val Typography = Typography(

    //top right
    displayLarge = TextStyle(
        fontFamily = Lato,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 24.sp,
        color = immutableDark
    ),

    bodyMedium = TextStyle(
        fontFamily = Lato,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = (21.6).sp,
        color = immutableDark
    ),

    displayMedium = TextStyle(
        fontFamily = Lato,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = (21.6).sp,
        color = Color.White //onPrimary
    ),

    //bottomsheet
    labelMedium = TextStyle(
        fontFamily = Lato,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = (21.6).sp,
        color = Color.White  //onPrimary
    ) //copy for bottomsheet end text


)

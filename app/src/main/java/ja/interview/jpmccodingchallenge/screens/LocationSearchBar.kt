package ja.interview.jpmccodingchallenge.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.google.android.gms.maps.model.LatLng
import ja.interview.jpmccodingchallenge.screens.weather.SearchState
import kotlinx.collections.immutable.toImmutableList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationSearchBar(
    state: SearchState,
    queryValue: (String) -> Unit,
    locationSelected: (LatLng) -> Unit,
    modifier: Modifier
) {
    var expanded by remember { mutableStateOf(state.isSearching) }
    val heightTextFields by remember { mutableStateOf(55.dp) }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    var editText by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .padding(30.dp)
            .fillMaxWidth()
            .clickable(
                onClick = { expanded = false }
            )
    ) {

        Column(modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.fillMaxWidth()) {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(heightTextFields)
                        .border(
                            width = 1.8.dp,
                            color = Color.Black,
                            shape = RoundedCornerShape(15.dp)
                        )
                        .onGloballyPositioned { coordinates ->
                            textFieldSize = coordinates.size.toSize()
                        },
                    value = editText,
                    onValueChange = {
                        editText = it
                        expanded = true
                        queryValue(it)
                    },
                    placeholder = { Text("Search: ex :NYC, New York, 10282") },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = Color.Black
                    ),
                    textStyle = TextStyle(
                        color = Color.Black,
                        fontSize = 16.sp
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    singleLine = true,
                )
            }
            AnimatedVisibility(visible = expanded) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color.LightGray,
                    ),
                ) {
                    LazyColumn(
                        modifier = Modifier.padding(bottom = 10.dp),
                    ) {
                        items(state.searchSuggestions.toImmutableList()) { item ->
                            ItemsCategory(item.locationDisplayText) {
                                expanded = false
                                locationSelected(item.lat)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ItemsCategory(
    title: String,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() }
            .padding(10.dp)
    ) {
        Text(text = title, fontSize = 16.sp)
    }
}
package com.example.myshoppinglistapp.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.nio.file.WatchEvent

data class ShoppingItem(val id:Int, var name:String, var quantity:Int, var isEditing:Boolean = false)

@Composable
fun ShoppingListApp(){
    var SItems by remember{ mutableStateOf(listOf<ShoppingItem>()) }
    var showDialog by remember { mutableStateOf(false)}
    var itemname by remember { mutableStateOf("")}
    var itemquantity by remember { mutableStateOf("")}

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ){
        Button(
            onClick = {showDialog=true},
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Add item")
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(SItems){
                    item ->
                if (item.isEditing){
                    ShoppingItemEditor(item = item , onEditComplete = {
                            editedname, editedquantity ->
                        SItems = SItems.map { it.copy(isEditing = false) }
                        val editedItem = SItems.find { it.id == item.id }
                        editedItem?.let {
                            it.name = editedname
                            it.quantity = editedquantity
                        }
                    })
                }else{
                    ShoppingListItem(item = item, onEditClick = {
                        //this line helps us in finding out which item we are currently edting and changing is "isEditing boolean" to true
                        SItems = SItems.map {it.copy(isEditing = it.id==item.id)}
                    },
                        onDeleteClick = {
                            SItems = SItems - item // this used to delete items
                        })
                }
            }
        }
    }
    if (showDialog){
        AlertDialog(onDismissRequest = { showDialog=false}, confirmButton = {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween){
                Button(onClick = {
                    if (itemname.isNotBlank()){
                        val newItem = ShoppingItem(id= SItems.size+1,
                            name = itemname,
                            quantity = itemquantity.toInt()
                        )
                        SItems += newItem
                        showDialog = false
                        itemname = ""
                    }
                }) {
                    Text("Add")
                }
                Button(onClick = {showDialog= false}) {
                    Text("Cancel")
                }
            }
        },
            title={ Text("Add Shopping item")},
            text={
                Column {
                    OutlinedTextField(value =itemname , onValueChange ={itemname = it},
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                    OutlinedTextField(value =itemquantity , onValueChange ={itemquantity = it},
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                }
            })
    }
}

@Composable
fun ShoppingItemEditor(item: ShoppingItem, onEditComplete:(String,Int ) -> Unit ){
    var editedname by remember { mutableStateOf(item.name)}
    var editedquantity by remember { mutableStateOf(item.quantity.toString())}
    var isEditing by remember { mutableStateOf(item.isEditing)}

    Row(modifier = Modifier
        .fillMaxWidth()
        .background(Color.White)
        .padding(8.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
        Column {
            BasicTextField(value = editedname, onValueChange = {editedname = it}, singleLine = true , modifier = Modifier
                .wrapContentSize()
                .padding(8.dp))
            BasicTextField(value = editedquantity, onValueChange = {editedquantity = it}, singleLine = true , modifier = Modifier
                .wrapContentSize()
                .padding(8.dp))
        }
        Button(onClick = { isEditing = false
            onEditComplete(editedname,editedquantity.toIntOrNull()?:1)
        }) {
            Text("Save")
        }
    }
}

@Composable
fun ShoppingListItem(item: ShoppingItem,
                     onEditClick:() -> Unit, // -> symbol is used to return something, () -> Unit this is lambda function it is executed when a edit action action triggered and it doesn't take in any parameter and doesnt return any value
                     onDeleteClick: () -> Unit,
){
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .border(
                border = BorderStroke(2.dp, Color(0XFF018786)),
                shape = RoundedCornerShape(20)
            )
    ){
        Text(text = item.name, modifier = Modifier.padding(8.dp))
        Text(text ="Quantity: ${item.quantity}", modifier = Modifier.padding(8.dp))
        Row (modifier = Modifier.padding(8.dp)){
            IconButton(onClick = onEditClick) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
            }
            IconButton(onClick = onDeleteClick) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }
        }
    }
}











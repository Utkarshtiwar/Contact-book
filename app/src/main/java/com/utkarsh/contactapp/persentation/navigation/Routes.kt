package com.utkarsh.contactapp.persentation.navigation

sealed class Routes (var route:String){
    object AddEdit:Routes("add_edit_Screen")
    object Home:Routes("home_Screen")

}
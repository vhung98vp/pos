import React, { Component } from "react";
import { Switch, Route } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";
import "./App.css";

import AuthService from "./services/auth.service";

import Login from "./components/user/login.component.jsx";
import Register from "./components/user/register.component.jsx";
import Home from "./components/home.component.jsx";
import Profile from "./components/user/profile.component.jsx";
import Categories from "./components/category/categories.component.jsx";
import NewCategory from "./components/category/new-category.component.jsx";
import UpdateCategory from "./components/category/update-category.component.jsx";
import Products from "./components/product/products.component.jsx";
import NewProduct from "./components/product/new-product.component.jsx";
import UpdateProduct from "./components/product/update-product.component.jsx";
import BoardManager from "./components/board/board-manager.component.jsx";
import BoardAdmin from "./components/board/board-admin.component.jsx";
import BoardWarehouse from "./components/board/board-warehouse.component.jsx";
import BoardCashier from "./components/board/board-cashier.component.jsx";
import Update from "./components/user/update.component";
import NewOrder from "./components/order/new-order.component";
import OrderDetails from "./components/order/details-order.component";
import NewImport from "./components/order/new-import.component";
import ImportDetails from "./components/order/details-import.component";

import { MDBContainer, MDBNavbar, MDBNavbarBrand, MDBNavbarNav, MDBNavbarToggler, MDBCollapse, MDBNavItem, MDBNavLink } from 'mdbreact';

class App extends Component {
  constructor(props) {
    super(props);
    this.logOut = this.logOut.bind(this);

    this.state = {
      showManagerBoard: false,
      showAdminBoard: false,
      currentUser: undefined,
    };
  }

  componentDidMount() {
    const user = AuthService.getCurrentUser();
    document.title = "Pos Application";

    if (user) {
      this.setState({
        currentUser: user,
        showAdminBoard: user.roles.includes("ROLE_ADMIN"),
        showManagerBoard: (user.roles.includes("ROLE_MANAGER") || user.roles.includes("ROLE_ADMIN")),
        showWarehouseBoard: (user.roles.includes("ROLE_WAREHOUSE") || user.roles.includes("ROLE_ADMIN")),
        showCashierBoard: (user.roles.includes("ROLE_CASHIER") || user.roles.includes("ROLE_ADMIN"))
      });
    }
  }

  logOut() {
    AuthService.logout();
    window.location.href = "/"
  }

  render() {
    const color = {backgroundColor: '#212529'}
    const { currentUser, showAdminBoard, showManagerBoard, showWarehouseBoard, showCashierBoard } = this.state;

    return (
      <div className="">        
        <header>
          <MDBNavbar style={color} fixed="top" dark expand="md">
              <MDBContainer>
                <MDBNavbarBrand href="/">
                  <strong>POS App</strong>
                </MDBNavbarBrand>
                <MDBNavbarToggler onClick={this.onClick} />
                <MDBCollapse isOpen={this.state.collapse} navbar>
                  <MDBNavbarNav left>

                    {showAdminBoard &&
                    <MDBNavItem>
                      <MDBNavLink to="/admin">Admin</MDBNavLink>
                    </MDBNavItem>
                    }

                    {showManagerBoard &&
                    <MDBNavItem>
                      <MDBNavLink to="/manager">Manager</MDBNavLink>
                    </MDBNavItem>
                    }

                    {showWarehouseBoard &&
                    <MDBNavItem>
                      <MDBNavLink to="/warehouse">Warehouse</MDBNavLink>
                    </MDBNavItem>
                    }

                    {showCashierBoard &&
                    <MDBNavItem>
                      <MDBNavLink to="/cashier">Cashier</MDBNavLink>
                    </MDBNavItem>
                    }

                  </MDBNavbarNav>

                  <MDBNavbarNav left>

                    {
                    <MDBNavItem>
                      <MDBNavLink to="/category">Categories</MDBNavLink>
                    </MDBNavItem>
                    }
                    
                    {
                    <MDBNavItem>
                      <MDBNavLink to="/product">Products</MDBNavLink>
                    </MDBNavItem>
                    }
  
                  </MDBNavbarNav>

                  {currentUser ? (
                  <MDBNavbarNav right>
                  
                    <MDBNavItem>
                      <MDBNavLink to="/profile">{currentUser.username}</MDBNavLink>
                    </MDBNavItem>

                    <MDBNavItem>
                      <MDBNavLink to="/" onClick={this.logOut}>Logout</MDBNavLink>
                    </MDBNavItem>
                    
                  </MDBNavbarNav>
                  ) : (
                    <MDBNavbarNav right>
                  
                    <MDBNavItem>
                      <MDBNavLink to="/register">Register</MDBNavLink>
                    </MDBNavItem>

                    <MDBNavItem>
                      <MDBNavLink to="/login">Login</MDBNavLink>
                    </MDBNavItem>
                    
                  </MDBNavbarNav>)}

                </MDBCollapse>
              </MDBContainer>
            </MDBNavbar>
          </header>        
        <div>
        <Switch>
            <Route exact path={["/", "/home"]} component={Home} />
            <Route exact path="/login" component={Login} />
            <Route exact path="/register" component={Register} />
            <Route exact path="/profile" component={Profile} />
            <Route exact path="/category" component={Categories} />
            <Route exact path="/category/add" component={NewCategory} />
            <Route exact path="/category/:id" component={UpdateCategory} />
            <Route exact path="/product" component={Products} />
            <Route exact path="/product/add" component={NewProduct} />
            <Route exact path="/product/:id" component={UpdateProduct} />
            <Route exact path="/order/add" component={NewOrder} />
            <Route exact path="/order/:id" component={OrderDetails} />
            <Route exact path="/import/add" component={NewImport} />
            <Route exact path="/import/:id" component={ImportDetails} />
            <Route path="/user/:id" component={Update} />
            <Route path="/warehouse" component={BoardWarehouse} />
            <Route path="/cashier" component={BoardCashier} />
            <Route path="/manager" component={BoardManager} />
            <Route path="/admin" component={BoardAdmin} />
            </Switch>
        </div>
      </div>
    );
  }
}

export default App;

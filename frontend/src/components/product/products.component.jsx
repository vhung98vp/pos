import React, { Component } from "react";
import authService from "../../services/auth.service";

import ProductService from "../../services/product.service";
import categoryService from "../../services/category.service";

export default class Products extends Component {
  constructor(props) {
    super(props);

    this.state = {
      products: [],
      categories: [],
      categoryId: -1,
      currentUser: authService.getCurrentUser(),
      content:'',
      listCategories: [],
      listProducts: [],
      show: false
    };
    this.deleteProduct = this.deleteProduct.bind(this);
  }

  componentDidMount() {
    ProductService.getAllProducts().then(
      response => {
        this.setState({
          products: response.data,
          content: "Product"
        }); 
        categoryService.getListParent(-1).then(
          response => {
            this.setState({
              listCategories: response.data
            });
            let listProducts = [];
            this.state.listCategories.forEach(category => {
              let list  = this.state.products;
              listProducts.push(list.filter(product => product.categoryId === category.id));
            })
            this.setState({listProducts: listProducts});

          },
          error => {}
        );
      },
      error => {
        this.setState({
          content:
            (error.response &&
              error.response.data &&
              error.response.data.message) ||
            error.message ||
            error.toString()
        });
      }
    );
    if (this.state.currentUser){
      if (this.state.currentUser.roles.includes("ROLE_ADMIN") || this.state.currentUser.roles.includes("ROLE_MANAGER")) 
      this.setState({show: true});
    }
  }

  updateProduct(id){
    this.props.history.push("/product/" + id);
  }

  deleteProduct(id){
    ProductService.deleteProduct(id).then(res => {
      this.setState({
        products: this.state.products.filter(item => item.id !== id)
      });
    })
  }

  render() {
    return (
      <div className="jumbotron">
          <h3>{this.state.content}</h3>

          {this.state.content === "Product" &&
                <div className = "row">                  
                    {this.state.show && <button className = "btn btn-primary" onClick = {() => this.props.history.push("/product/add")}>New Product</button>}
                    
                    <table className = "table table-striped table-bordered">
                        <thead>
                            <tr>
                                <th>Category</th>
                                <th>Picture</th>
                                <th>Product Name</th>
                                <th>Price</th>
                                <th>Quantity</th>
                                <th>Unit</th>            
                                {this.state.show && <th>Actions</th>}
                            </tr>
                        </thead>
                        <tbody>
                            {
                                this.state.listProducts.map(category =>
                                  category.map(product => 
                                  <tr key = {product.id}>
                                            <td>{this.state.listCategories.find(item => item.id === product.categoryId).name}</td>
                                            <td> <img alt="pic" src={product.pictureUri === null ? 
                                                    "https://trentini.com.br/wp-content/uploads/2016/04/dummy-post-square-1-768x768.jpg" : product.pictureUri
                                                    } height="200px" width="200px" /> </td>
                                            <td> {product.name} </td>   
                                            <td> {product.sellPrice}</td>
                                            <td> {product.quantity}</td>
                                            <td> {product.unit}</td>
                                            {this.state.show && <td>
                                              <button onClick={ () => this.updateProduct(product.id)} className="btn btn-success">Update </button>                                            
                                              <button style={{marginLeft: "10px"}} onClick={ () => this.deleteProduct(product.id)} className="btn btn-danger">Delete</button>
                                            </td>}
                                    </tr>
                                  )
                                )
                              }
                              
                        </tbody>
                    </table>
                    
                </div>
            }
      </div>
    );
  }
}

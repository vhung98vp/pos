import React, { Component } from "react";

import orderService from "../../services/order.service";

export default class OrderDetails extends Component{
    constructor(props) {
        super(props);
    
        this.state = {
          id: parseInt(this.props.match.params.id),
          total: 0,
          note: "",
          createOn: "",
          updateOn: "",
          username: "",
          status: "",
          products: []
        };
    }

    componentDidMount() {
        orderService.getOrderById(this.state.id).then(
            response => {
                this.setState({
                    content: "Order details",
                    total: response.data.total,
                    note: response.data.note,
                    createOn: response.data.createOn,
                    updateOn: response.data.updateOn,
                    username: response.data.username,
                    status: response.data.status,
                    products: response.data.orderProducts
                });
            },
            error => {}
        )
    }

    render() {
        return (
            <div className="container">
              <div className="jumbotron">                  
                <h3>{this.state.content}</h3>
                <div class="row">
                    <table className = "table table-striped table-bordered">
                        <thead></thead>
                        <tbody>
                        <tr>
                            <td>Order Number</td>
                            <td># {this.state.id} </td>
                        </tr>
                        <tr>
                            <td>Order By</td>
                            <td>{this.state.username} </td>
                        </tr>
                        <tr>
                            <td>Status</td>
                            <td>{this.state.status} </td>
                        </tr>
                        <tr>
                            <td>Created On</td>
                            <td>{this.state.createOn} </td>
                        </tr>
                        <tr>
                            <td>Update On</td>
                            <td>{this.state.updateOn} </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="row">
                    <table className = "table table-striped table-bordered">
                        <thead>
                            <tr>
                                <td>No</td>
                                <td>Product</td>
                                <td>Quantity</td>
                                <td>Price</td>
                                <td>Total</td>
                            </tr>
                        </thead>
                        <tbody>
                            {
                                this.state.products.map(product =>
                                    <tr key = {product.productId}>
                                        <td>{this.state.products.indexOf(product) + 1}</td>
                                        <td>{product.productName}</td>
                                        <td>{product.quantity}</td>
                                        <td>{product.sellPrice} $</td>
                                        <td>{product.sellPrice * product.quantity} $</td>
                                    </tr>
                                )
                            }
                            <tr>
                                <td colSpan="4">Total</td>
                                <td>{this.state.total} $</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
              </div>
            </div>

        );
    }
}

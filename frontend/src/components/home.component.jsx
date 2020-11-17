import React, { Component } from "react";

import UserService from "../services/user.service";

export default class Home extends Component {
  constructor(props) {
    super(props);

    this.state = {
      content: ""
    };
  }

  componentDidMount() {
    UserService.getPublicContent().then(
      response => {
        this.setState({
          content: response.data
        });
      },
      error => {
        this.setState({
          content:
            (error.response && error.response.data) ||
            error.message ||
            error.toString()
        });
      }
    );
  }

  render() {
    return (
      <div className="background">
        <div className="hero-text-box">
                <h1>{this.state.content}</h1>
                <a className="btn btn-full" href="/category">View categories</a>
                <a className="btn btn-ghost" href="/product">View products</a>
        </div>
      </div>
    );
  }
}

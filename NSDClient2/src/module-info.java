module NSDClient2 {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.swing;
    requires javafx.swt;
    requires javafx.media;
    requires json.simple;
    opens nsd.se.shu.views;
    opens nsd.se.shu.models;
    opens nsd.se.shu.controllers;
    opens nsd.se.shu.services;
}
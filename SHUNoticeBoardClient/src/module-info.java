module SHUNoticeBoardClient {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.swing;
    requires javafx.swt;
    requires javafx.media;
    requires json.simple;
    opens shu.nsd.controllers;
    opens shu.nsd.utils;
    opens shu.nsd;
}
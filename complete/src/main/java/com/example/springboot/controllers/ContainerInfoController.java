package com.example.springboot.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class ContainerInfoController {

    @GetMapping("/container-info")
    public String getContainerInfo(Model model) {
        // Get System Properties
        String architecture = System.getProperty("os.arch"); // e.g., amd64, s390x
        String osName = System.getProperty("os.name");
        String osVersion = System.getProperty("os.version");
        String javaVersion = System.getProperty("java.version");

        // Get Hostname (Pod Name)
        String hostname = getHostName();

        // Get Linux Kernel Version
        String kernelVersion = getKernelVersion();

        // Get Container CPU & Memory Limits (if running in Kubernetes)
        String cpuLimit = getCGroupValue("/sys/fs/cgroup/cpu/cpu.max");
        String memoryLimit = getCGroupValue("/sys/fs/cgroup/memory/memory.limit_in_bytes");

        // Add Data to Model
        model.addAttribute("architecture", architecture);
        model.addAttribute("hostname", hostname);
        model.addAttribute("osName", osName);
        model.addAttribute("osVersion", osVersion);
        model.addAttribute("javaVersion", javaVersion);
        model.addAttribute("kernelVersion", kernelVersion);
        model.addAttribute("cpuLimit", cpuLimit);
        model.addAttribute("memoryLimit", memoryLimit);

        return "container-info"; // This maps to src/main/resources/templates/container-info.html
    }

    private String getHostName() {
        try {
            return java.net.InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            return "Unknown";
        }
    }

    private String getKernelVersion() {
        try {
            return new String(Files.readAllBytes(Paths.get("/proc/version"))).trim();
        } catch (IOException e) {
            return "Unknown";
        }
    }

    private String getCGroupValue(String path) {
        try {
            return Files.lines(Paths.get(path)).collect(Collectors.joining("\n")).trim();
        } catch (IOException e) {
            return "Not available";
        }
    }
}

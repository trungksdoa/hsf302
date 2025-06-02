@GetMapping
public String home(Model model, HttpServletRequest request) {
    Map<String, String> contactInfor = new HashMap<>();
    contactInfor.put("phone","0335857132");
    contactInfor.put("email","fpt@fpt.com");
    contactInfor.put("address","FPT");
    
    model.addAttribute("showSidebar", false);
    System.out.println("HOME: showSidebar set to false"); // Debug log
    
    model.addAttribute("contactInfor", contactInfor);
    model.addAttribute("title", "Home");
    model.addAttribute("content", "view/home");
    
    if (request != null) {
        model.addAttribute("httpServletRequest", request);
        System.out.println("HOME: Request URI = " + request.getRequestURI()); // Debug log
    }
    
    return "view/layout";
}

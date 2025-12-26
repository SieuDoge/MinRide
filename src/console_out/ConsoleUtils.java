package console_out;

import java.util.Scanner;

public class ConsoleUtils {
    public static final Scanner scanner = new Scanner(System.in);

    // --- COLORS & STYLING ---
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String BOLD = "\u001B[1m";
    public static final String ITALIC = "\u001B[3m";

    // --- MENU CONFIG ---
    private static final int MENU_WIDTH = 70; // Standard Width

    public static void clearConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            for(int i = 0; i < 50; i++) System.out.println();
        }
    }

    // --- HELPER: ANSI AWARE LENGTH ---
    public static int getVisibleLength(String str) {
        if (str == null) return 0;
        return str.replaceAll("\u001B\\[[;\\d]*m", "").length();
    }

    // --- NEW: THEME ENGINE ---
    
    public static void printHeader(String title) {
        // Draw Top Border: ╔══════╗
        System.out.print(CYAN + "╔");
        for(int i=0; i<MENU_WIDTH; i++) System.out.print("═");
        System.out.println("╗" + RESET);
        
        // Draw Title: ║  TITLE  ║
        printCenteredRow("║", title.toUpperCase(), BOLD + CYAN);
        
        // Draw Separator: ╠══════╣
        System.out.print(CYAN + "╠");
        for(int i=0; i<MENU_WIDTH; i++) System.out.print("═");
        System.out.println("╣" + RESET);
    }
    
    public static void printOption(int key, String desc) {
        // Format: ║  1. Description               ║
        // We need to calculate padding manually to avoid alignment issues with colors
        
        String prefix = "  " + key + ". ";
        String content = prefix + desc;
        int contentLen = getVisibleLength(content);
        int paddingRight = MENU_WIDTH - contentLen - 1; // -1 for padding left space
        
        System.out.print(CYAN + "║" + RESET);
        System.out.print(" " + content); // 1 space left padding
        for(int i=0; i<paddingRight; i++) System.out.print(" ");
        System.out.println(CYAN + "║" + RESET);
    }

    public static void printLine() {
        // Draw Bottom Border: ╚══════╝
        System.out.print(CYAN + "╚");
        for(int i=0; i<MENU_WIDTH; i++) System.out.print("═");
        System.out.println("╝" + RESET);
    }
    
    // Generic Helper for centered row with border
    public static void printCenteredRow(String border, String text, String color) {
        int textLen = getVisibleLength(text);
        int padding = (MENU_WIDTH - textLen) / 2;
        int paddingRight = MENU_WIDTH - textLen - padding;
        
        System.out.print(CYAN + border + RESET);
        for(int i=0; i<padding; i++) System.out.print(" ");
        System.out.print(color + text + RESET);
        for(int i=0; i<paddingRight; i++) System.out.print(" ");
        System.out.println(CYAN + border + RESET);
    }
    
    // Helper for Borders
    public static void printBorderLine(String start, String mid, String end) {
        System.out.print(CYAN + start);
        for (int i = 0; i < MENU_WIDTH; i++) System.out.print(mid);
        System.out.println(end + RESET);
    }
    
    // --- INFO CARD BUILDER ---
    public static void printInfoCard(String title, String[] labels, String[] values) {
        if (labels.length != values.length) return;
        
        // 1. Calculate max lengths
        int maxLabel = 0;
        int maxValue = 0;
        for(String s : labels) maxLabel = Math.max(maxLabel, getVisibleLength(s));
        for(String s : values) maxValue = Math.max(maxValue, getVisibleLength(s));
        
        // 2. Determine Card Width
        // Content structure: "| Label... : Value... |"
        // Space logic: "| " (2) + Label (maxLabel) + ": " (2) + Value (maxValue) + " |" (2)
        // Total minimal width = 2 + maxLabel + 2 + maxValue + 2 = maxLabel + maxValue + 6;
        int minContentWidth = maxLabel + maxValue + 6;
        int cardWidth = Math.max(MENU_WIDTH, minContentWidth); // cardWidth is the INNER width (dashes count)
        
        // 3. Draw Top Border
        System.out.print(CYAN + "┌");
        for(int i=0; i<cardWidth; i++) System.out.print("─");
        System.out.println("┐" + RESET);
        
        // 4. Draw Title
        int titleLen = getVisibleLength(title);
        int padLeft = (cardWidth - titleLen) / 2;
        int padRight = cardWidth - titleLen - padLeft;
        
        System.out.print(CYAN + "│" + RESET);
        for(int i=0; i<padLeft; i++) System.out.print(" ");
        System.out.print(BOLD + YELLOW + title.toUpperCase() + RESET);
        for(int i=0; i<padRight; i++) System.out.print(" ");
        System.out.println(CYAN + "│" + RESET);
        
        // 5. Draw Separator
        System.out.print(CYAN + "├");
        for(int i=0; i<cardWidth; i++) System.out.print("─");
        System.out.println("┤" + RESET);
        
        // 6. Draw Content Rows
        for(int i=0; i<labels.length; i++) {
            String label = labels[i];
            String value = values[i];
            
            System.out.print(CYAN + "│ " + RESET); // Left Border + Space
            
            // Print Label (Padded)
            // Need to handle padding manually for ANSI safety
            System.out.print(YELLOW + label + ":" + RESET);
            int labelVis = getVisibleLength(label) + 1; // +1 for ':'
            int labelPad = (maxLabel + 1) - labelVis; // +1 to match maxLabel + ':' width
            for(int k=0; k<labelPad; k++) System.out.print(" ");
            
            System.out.print(" "); // Separator space
            
            // Print Value
            System.out.print(value);
            int valueVis = getVisibleLength(value);
            
            // Calculate Remaining Right Padding to hit Border
            // Used so far: "│ " (visual 2?? No, border is 0 width in logic, inner is what matters)
            // Inner used: 1 (space) + labelVis + labelPad + 1 (space) + valueVis
            // Total Inner Used = 1 + (maxLabel + 1) + 1 + valueVis
            
            int totalInnerUsed = 1 + (maxLabel + 1) + 1 + valueVis;
            int rightPad = cardWidth - totalInnerUsed;
            
            for(int k=0; k<rightPad; k++) System.out.print(" ");
            
            System.out.println(CYAN + "│" + RESET); // Right Border
        }
        
        // 7. Bottom Border
        System.out.print(CYAN + "└");
        for(int i=0; i<cardWidth; i++) System.out.print("─");
        System.out.println("┘" + RESET);
    }

    // --- TABLE BUILDER (KEPT BUT UPDATED COLORS) ---
    public static void printTable(String[] headers, String[][] data) {
        if (headers == null || headers.length == 0) return;

        // 1. Calculate Column Widths
        int[] colWidths = new int[headers.length];
        for (int i = 0; i < headers.length; i++) {
            colWidths[i] = headers[i].length();
        }

        if (data != null) {
            for (String[] row : data) {
                for (int i = 0; i < row.length && i < headers.length; i++) {
                    int len = (row[i] != null) ? getVisibleLength(row[i]) : 0; // Fix: Use visible length
                    if (len > colWidths[i]) {
                        colWidths[i] = len;
                    }
                }
            }
        }

        // Add padding
        for (int i = 0; i < colWidths.length; i++) colWidths[i] += 4; 

        // 2. Print Top Border
        System.out.print(CYAN + "╔");
        for (int i = 0; i < colWidths.length; i++) {
            for (int j = 0; j < colWidths[i]; j++) System.out.print("═");
            if (i < colWidths.length - 1) System.out.print("╦");
        }
        System.out.println("╗" + RESET);

        // 3. Print Headers
        System.out.print(CYAN + "║" + RESET);
        for (int i = 0; i < headers.length; i++) {
            System.out.print(BOLD + padCenter(headers[i], colWidths[i]) + RESET);
            System.out.print(CYAN + "║" + RESET);
        }
        System.out.println();

        // 4. Print Separator
        System.out.print(CYAN + "╠");
        for (int i = 0; i < colWidths.length; i++) {
            for (int j = 0; j < colWidths[i]; j++) System.out.print("═");
            if (i < colWidths.length - 1) System.out.print("╬");
        }
        System.out.println("╣" + RESET);

        // 5. Print Data
        if (data != null) {
            for (String[] row : data) {
                System.out.print(CYAN + "║" + RESET);
                for (int i = 0; i < headers.length; i++) {
                    String cell = (i < row.length && row[i] != null) ? row[i] : "";
                    if (isNumeric(cell)) {
                        System.out.print(padLeft(cell, colWidths[i])); // Helper handles padding
                    } else {
                        System.out.print(padRight(cell, colWidths[i]));
                    }
                    System.out.print(CYAN + "║" + RESET);
                }
                System.out.println();
            }
        }

        // 6. Print Bottom
        System.out.print(CYAN + "╚");
        for (int i = 0; i < colWidths.length; i++) {
            for (int j = 0; j < colWidths[i]; j++) System.out.print("═");
            if (i < colWidths.length - 1) System.out.print("╩");
        }
        System.out.println("╝" + RESET);
    }

    private static String padRight(String s, int n) {
        int visible = getVisibleLength(s);
        int pad = n - visible;
        if(pad <= 0) return s; // Should not happen if width calc is correct
        // Add 2 padding left/right concept from table builder logic
        // But logic above added +4 to width.
        // Let's simplified: Center in cell? Or Left + 2 spaces.
        // The previous logic was " " + cell + " ".
        // Let's rebuild pad logic:
        StringBuilder sb = new StringBuilder();
        sb.append("  ").append(s);
        for(int i=0; i<pad-2; i++) sb.append(" ");
        return sb.toString();
    }

    private static String padLeft(String s, int n) {
        int visible = getVisibleLength(s);
        int pad = n - visible;
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<pad-2; i++) sb.append(" ");
        sb.append(s).append("  ");
        return sb.toString();
    }

    private static String padCenter(String s, int n) {
        int visible = getVisibleLength(s);
        int padding = n - visible;
        int left = padding / 2;
        int right = padding - left;
        
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<left; i++) sb.append(" ");
        sb.append(s);
        for(int i=0; i<right; i++) sb.append(" ");
        return sb.toString();
    }

    private static boolean isNumeric(String str) {
        if (str == null) return false;
        String clean = str.replaceAll(",", "").replaceAll(" VND", "").replaceAll(" km", "").trim();
        return clean.matches("-?\\d+(\\.\\d+)?");
    }

    // --- INPUTS ---
    public static void printSuccess(String msg) {
        System.out.println(GREEN + "✔ THÀNH CÔNG: " + msg + RESET);
    }

    public static void printError(String msg) {
        System.out.println(RED + "✘ LỖI: " + msg + RESET);
    }

    public static void pause() {
        System.out.println("\n" + ITALIC + "[Nhấn Enter để tiếp tục...]" + RESET);
        scanner.nextLine();
    }

    public static int getIntInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.println(RED + "Vui lòng nhập số nguyên!" + RESET);
            scanner.next();
            System.out.print(prompt);
        }
        int input = scanner.nextInt();
        scanner.nextLine();
        return input;
    }

    public static double getDoubleInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextDouble()) {
            System.out.println(RED + "Vui lòng nhập số thực!" + RESET);
            scanner.next();
            System.out.print(prompt);
        }
        double input = scanner.nextDouble();
        scanner.nextLine();
        return input;
    }

    public static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
}
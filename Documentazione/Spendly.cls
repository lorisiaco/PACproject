\NeedsTeXFormat{LaTeX2e}
\ProvidesClass{Spendly}[2024/12/03 Custom class for Spendly project]

\LoadClass{report} % Basato sulla classe report

\RequirePackage[utf8]{inputenc}
\RequirePackage[italian]{babel}
\RequirePackage{geometry}
\RequirePackage{graphicx}

% Impostazioni della pagina
\geometry{portrait, margin=3cm}

% Definizione del comando frontespizio
\newcommand{\frontespizio}[6]{
    \begin{titlepage}
        \centering
        {\Huge \textbf{#1}\par} % Titolo principale
        \vspace{2cm}
        {\Large #2\par} % Nome del corso
        \vspace{1cm}
        {\large #3\par} % Nome della professoressa
        \vspace{1cm}
        {\large #4\par} % Anno accademico
        \vfill
        {\large \textbf{#5}\par} % Autori
        \vspace{1cm}
        {\small #6\par} % Eventuali note aggiuntive
    \end{titlepage}
}
\endinput
